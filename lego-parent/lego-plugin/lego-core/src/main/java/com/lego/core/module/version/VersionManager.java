package com.lego.core.module.version;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import com.lego.core.common.Constants;
import com.lego.core.data.DataSourceConfig;
import com.lego.core.enums.ExceptionEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class VersionManager implements InitializingBean {

    private static final String INIT_SQL_FOLDER = "init";
    private static final String SELECT_VERSION_SQL = "SELECT value FROM sys_config WHERE code = 'AppVersion'";
    private static final String UPDATE_VERSION_SQL = "UPDATE sys_config SET value = ? WHERE code = 'AppVersion'";

    private boolean needInit = true;
    private static volatile boolean initializing = false;

    @Value("${auto-run-sql:false}")
    private boolean autoRunSql;

    @Autowired
    private SqlFileLoader fileLoader;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!autoRunSql) {
            return;
        }
        log.info("启动自检查更新流程auto-run-sql={}", autoRunSql);
        try (Connection connection = dataSourceConfig.getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tableResult = metaData.getTables(null, null, "sys_config", null);
            if (!tableResult.next()) {
                this.needInit = true;
                return;
            }
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(SELECT_VERSION_SQL);
            this.needInit = !result.next();
            if (this.needInit) {
                return;
            }
            String currentVersion = result.getString("value");
            if (fileLoader.hasNewVersion(currentVersion)) {
                execUpdate();
            }
        }
    }

    public boolean needInit() {
        return needInit && autoRunSql;
    }

    public String execUpdate() {
        BusinessException.check(autoRunSql, "未开启autoRunSql配置，自动更新失败！");
        try (Connection connection = dataSourceConfig.getDataSource().getConnection()) {
            String currentVersion = getCurrentVersion(connection);
            if (fileLoader.hasNewVersion(currentVersion)) {
                String newVersion = fileLoader.getNewVersion(currentVersion);
                log.info("当前系统版本[{}]非最新版本[{}]，启动版本更新流程", currentVersion, newVersion);
                Map<String, String> newVersions = fileLoader.loadNewVersion(currentVersion);
                runBatchSql(newVersions, connection, false);
                PreparedStatement pstmt = connection.prepareStatement(UPDATE_VERSION_SQL);
                pstmt.setString(1, newVersion);
                pstmt.executeUpdate();
                connection.commit();
                log.info("系统更新完成，当前版本为[{}]", newVersion);
                return newVersion;
            }
            return currentVersion;
        } catch (Exception e) {
            throw new CoreException(ExceptionEnum.UPDATE_ERROR, e);
        }
    }

    public String execInit() {
        BusinessException.check(autoRunSql, "未开启autoRunSql配置，自动更新失败！");
        BusinessException.check(needInit, "系统无需初始化操作！");
        BusinessException.check(!initializing, "初始化处理中，请稍后！");
        log.info("启动初始化执行器");
        Map<String, String> initSql = fileLoader.load(INIT_SQL_FOLDER);
        try (Connection connection = dataSourceConfig.getDataSource().getConnection()) {
            this.initializing = true;
            runBatchSql(initSql, connection, true);
            this.needInit = false;
            log.info("系统初始化完成");
            return getCurrentVersion(connection);
        } catch (Exception e) {
            throw new CoreException(ExceptionEnum.SQL_ERROR, e);
        } finally {
            this.initializing = false;
        }
    }

    public String getNewVersion(String currentVersion) {
        return fileLoader.getNewVersion(currentVersion);
    }

    private String getCurrentVersion(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(SELECT_VERSION_SQL);
        CoreException.check(result.next(), "获取系统版本失败！");
        String currentVersion = result.getString("value");
        BusinessException.check(StringUtil.isNotBlank(currentVersion), "当前版本过旧，未检测到版本信息！");
        return currentVersion;
    }

    private void runBatchSql(Map<String, String> sqlFiles, Connection connection, boolean autoCommit) throws IOException {
        if (!sqlFiles.isEmpty()) {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setDelimiter("~");
            runner.setLogWriter(null);
            runner.setAutoCommit(autoCommit);
            runner.setStopOnError(true);
            for (Map.Entry<String, String> sqlFile : sqlFiles.entrySet()) {
                String name = sqlFile.getKey();
                String content = sqlFile.getValue();
                runSql(name, content, runner);
            }
        }
    }

    private void runSql(String name, String content, ScriptRunner runner) {
        log.info("执行脚本：{}", name);
        String fileType = StringUtil.substringLastAfter(name, ".");
        if ("sql".equals(fileType)) {
            Pattern pattern = Pattern.compile("--.*", Pattern.MULTILINE);
            content = ReUtil.replaceAll(content, pattern, "");
            pattern = Pattern.compile("[ \\t]*;$", Pattern.MULTILINE);
            content = ReUtil.replaceAll(content, pattern, "~");
            pattern = Pattern.compile("\bINSERT\\s+INTO\b", Pattern.MULTILINE);
            content = ReUtil.replaceAll(content, pattern, "INSERT /*+ append */ INTO");
        } else {
            content = ReUtil.replaceAll(content, "[ \\t]*;$", "~");
        }
        InputStream inputStream = IoUtil.toStream(content, Constants.DEFAULT_CHARSET);
        if (null != inputStream) {
            runner.runScript(new InputStreamReader(inputStream, Constants.DEFAULT_CHARSET));
            IoUtil.close(inputStream);
        }
    }
}
