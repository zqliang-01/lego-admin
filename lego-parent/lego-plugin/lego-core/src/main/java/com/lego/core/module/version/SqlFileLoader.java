package com.lego.core.module.version;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.lego.core.common.Constants;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class SqlFileLoader {

    private static final String UPDATE_SQL_FOLDER = "update";
    private static final String SQL_ZIP_FILE_PATH = "classpath:sql.zip";
    private static final String VERSION_REGEX = "([^/]*/){1}([^/]+)/?";

    public Map<String, String> load(String type) {
        Map<String, String> resultMap = new TreeMap<>(String::compareTo);
        try (InputStream inputStream = ResourceUtil.getStream(SQL_ZIP_FILE_PATH);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory() && entry.getName().startsWith(type)) {
                    String data = StrUtil.str(IoUtil.readBytes(zipInputStream, false), Constants.DEFAULT_CHARSET);
                    resultMap.put(entry.getName(), data);
                }
                entry = zipInputStream.getNextEntry();
            }
            return resultMap;
        } catch (Exception e) {
            throw new CoreException("加载SQL文件信息失败！", e);
        }
    }

    public List<String> loadName(String type) {
        List<String> names = new ArrayList<>();
        try (InputStream inputStream = ResourceUtil.getStream(SQL_ZIP_FILE_PATH);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory() && entry.getName().startsWith(type)) {
                    names.add(entry.getName());
                }
                entry = zipInputStream.getNextEntry();
            }
            names.sort(String::compareTo);
            return names;
        } catch (Exception e) {
            throw new CoreException("加载SQL文件名称信息失败！", e);
        }
    }

    public Map<String, String> loadNewVersion(String currentVersion) {
        Map<String, String> resultMap = new TreeMap<>(String::compareTo);
        try (InputStream inputStream = ResourceUtil.getStream(SQL_ZIP_FILE_PATH);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory() && entry.getName().startsWith(UPDATE_SQL_FOLDER)) {
                    String version = parseVersion(entry.getName());
                    if (StringUtil.isNewVersion(version, currentVersion)) {
                        String data = StrUtil.str(IoUtil.readBytes(zipInputStream, false), Constants.DEFAULT_CHARSET);
                        resultMap.put(entry.getName(), data);
                    }
                }
                entry = zipInputStream.getNextEntry();
            }
            return resultMap;
        } catch (Exception e) {
            throw new CoreException("加载新版本SQL文件信息失败！", e);
        }
    }

    public boolean hasNewVersion(String currentVersion) {
        if (StringUtil.isBlank(currentVersion)) {
            return false;
        }
        for (String name : new SqlFileLoader().loadName(UPDATE_SQL_FOLDER)) {
            String version = parseVersion(name);
            if (StringUtil.isNewVersion(version, currentVersion)) {
                return true;
            }
        }
        return false;
    }

    public String getNewVersion(String currentVersion) {
        String newVersion = currentVersion;
        for (String name : new SqlFileLoader().loadName(UPDATE_SQL_FOLDER)) {
            String version = parseVersion(name);
            if (StringUtil.isBlank(currentVersion) || StringUtil.isNewVersion(version, currentVersion)) {
                newVersion = version;
            }
        }
        return newVersion;
    }

    private String parseVersion(String name) {
        Matcher matcher = Pattern.compile(VERSION_REGEX).matcher(name);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }
}
