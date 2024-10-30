package com.lego.core.data;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lego.core.common.Constants;
import com.lego.core.common.ExceptionEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Component
public class VersionManager implements InitializingBean {

    private static final String INIT_SQL_FOLDER = "init";
    private static final String UPDATE_SQL_FOLDER = "update";
    private static final String SELECT_VERSION_SQL = "SELECT value FROM sys_config WHERE code = 'AppVersion'";
    private static final String UPDATE_VERSION_SQL = "UPDATE sys_config SET value = ? WHERE code = 'AppVersion'";

    private boolean needInit = true;
    private static volatile boolean initializing = false;

    @Value("${auto-run-sql:false}")
    private boolean autoRunSql;

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
            if (!this.needInit) {
                String newVersion = getNewVersion();
                String currentVersion = result.getString("value");
                if (!StringUtil.equals(currentVersion, newVersion)) {
                    log.info("发现新版本[{}]，执行自动更新操作", newVersion);
                    execUpdate();
                }
            }
        }
    }

    public boolean needInit() {
        return needInit && autoRunSql;
    }

    public String execUpdate() {
        BusinessException.check(autoRunSql, "未开启autoRunSql配置，自动更新失败！");
        try (Connection connection = dataSourceConfig.getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(SELECT_VERSION_SQL);
            CoreException.check(result.next(), "获取系统版本失败！");
            String currentVersion = result.getString("value");
            BusinessException.check(StringUtil.isNotBlank(currentVersion), "当前版本过旧，未检测到版本信息，更新失败！");

            List<File> sqlFiles = new ArrayList<>();
            String newVersion = getVersionSqlFile(currentVersion, sqlFiles);
            if (!StringUtil.equals(currentVersion, newVersion)) {
                log.info("当前系统版本{}非最新版本，启动版本更新流程", currentVersion);
                runBatchSql(sqlFiles, connection, false);
                PreparedStatement pstmt = connection.prepareStatement(UPDATE_VERSION_SQL);
                pstmt.setString(1, newVersion);
                pstmt.executeUpdate();
                connection.commit();
                log.info("系统更新完成，当前版本为[{}]", newVersion);
            }
            return newVersion;
        } catch (Exception e) {
            throw new CoreException(ExceptionEnum.UPDATE_ERROR, e);
        }
    }

    public String execInit() {
        BusinessException.check(autoRunSql, "未开启autoRunSql配置，自动更新失败！");
        BusinessException.check(needInit, "系统无需初始化操作！");
        BusinessException.check(!initializing, "初始化处理中，请稍后！");
        List<File> sqlFiles = getInitSqlFile();
        CoreException.check(!sqlFiles.isEmpty(), "读取初始化脚本异常！");
        log.info("启动初始化执行器");
        try (Connection connection = dataSourceConfig.getDataSource().getConnection()) {
            this.initializing = true;
            runBatchSql(sqlFiles, connection, true);
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(SELECT_VERSION_SQL);
            CoreException.check(result.next(), "获取系统版本失败！");
            this.needInit = false;
            log.info("系统初始化完成");
            return result.getString("value");
        } catch (Exception e) {
            throw new CoreException(ExceptionEnum.SQL_ERROR, e);
        } finally {
            this.initializing = false;
        }
    }

    private List<File> getInitSqlFile() {
        File rootFile = getProjectBaseDir();
        File file = getChildrenFile(rootFile, INIT_SQL_FOLDER);
        List<File> versionFiles = new ArrayList<>();
        for (File versionFile : file.listFiles()) {
            versionFiles.addAll(FileUtil.loopFiles(versionFile));
        }
        return versionFiles;
    }

    private String getVersionSqlFile(String version, List<File> sqlFiles) {
        String newVersion = version;
        File rootFile = getProjectBaseDir();
        File file = getChildrenFile(rootFile, UPDATE_SQL_FOLDER);
        for (File versionFile : file.listFiles()) {
            if (isNew(versionFile.getName(), version)) {
                newVersion = versionFile.getName();
                sqlFiles.addAll(FileUtil.loopFiles(versionFile));
            }
        }
        return newVersion;
    }

    private File getChildrenFile(File rootFile, String name) {
        for (File file : rootFile.listFiles()) {
            if (name.equals(file.getName())) {
                return file;
            }
        }
        throw new CoreException("SQL脚本路径[{0}]中未找到[{1}]信息！", rootFile, name);
    }

    private File getProjectBaseDir() {
        Resource resource = resourceLoader.getResource("classpath:sql.zip");
        try (InputStream inputStream = resource.getInputStream()) {
            FileUtil.del(FileUtil.getTmpDirPath() + File.separator + "LegoAdmin-sql");
            File targetFile = FileUtil.mkdir(FileUtil.getTmpDirPath() + File.separator + "LegoAdmin-sql");
            return ZipUtil.unzip(inputStream, targetFile, Constants.DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new CoreException("读取SQL更新脚本径失败！", e);
        }
    }

    public String getNewVersion() {
        Resource resource = resourceLoader.getResource("classpath:sql.zip");
        try (InputStream inputStream = resource.getInputStream();
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                if ("version.json".equals(entry.getName())) {
                    String versionData = IoUtil.read(zipInputStream, Constants.DEFAULT_CHARSET);
                    JSONObject version = JSON.parseObject(versionData);
                    return version.getString("version");
                }
                entry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            log.error("读取新版本信息失败！", e);
        }
        return "";
    }

    private boolean isNew(String version1, String version2) {
        int[] nums1 = Arrays.stream(version1.substring(1).split("\\."))
            .mapToInt(Integer::parseInt)
            .toArray();
        int[] nums2 = Arrays.stream(version2.substring(1).split("\\."))
            .mapToInt(Integer::parseInt)
            .toArray();

        int length = Math.max(nums1.length, nums2.length);
        for (int i = 0; i < length; i++) {
            int num1 = i < nums1.length ? nums1[i] : 0;
            int num2 = i < nums2.length ? nums2[i] : 0;

            if (num1 != num2) {
                return Integer.compare(num1, num2) > 0;
            }
        }
        return false;
    }

    private void runBatchSql(List<File> sqlFiles, Connection connection, boolean autoCommit) throws IOException {
        if (!sqlFiles.isEmpty()) {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setDelimiter("~");
            runner.setLogWriter(null);
            runner.setAutoCommit(autoCommit);
            runner.setStopOnError(true);
            for (File sqlFile : sqlFiles) {
                log.info("执行脚本：{}", sqlFile);
                runSql(sqlFile, runner);
            }
        }
    }

    private void runSql(File sqlFile, ScriptRunner runner) {
        String content = FileUtil.readString(sqlFile, Constants.DEFAULT_CHARSET);
        String fileType = FileTypeUtil.getType(sqlFile);
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
