package com.lego.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import com.lego.core.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class SqlRunnerUtil {

    public static void runBatchSql(List<File> sqlFiles, Connection connection, boolean autoCommit) throws IOException {
        if (!sqlFiles.isEmpty()) {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setDelimiter("~");
            runner.setLogWriter(null);
            runner.setAutoCommit(autoCommit);
            runner.setStopOnError(true);
            for (File sqlFile : sqlFiles) {
                String name = sqlFile.getName();
                String content = FileUtil.readString(sqlFile, Constants.DEFAULT_CHARSET);
                runSql(name, content, runner);
            }
        }
    }

    public static void runBatchSql(Map<String, String> sqlFiles, Connection connection, boolean autoCommit) throws IOException {
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

    private static void runSql(String name, String content, ScriptRunner runner) {
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
