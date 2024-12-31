package com.lego.core.web.upload;

import cn.hutool.core.io.FileUtil;
import com.lego.core.enums.FileLocationEnum;
import com.lego.core.dto.FileUploadInfo;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;

import java.io.File;
import java.io.InputStream;

public class LocalFileHandler implements FileHandler {

    private UploadConfig.LocalConfig config;

    public LocalFileHandler(UploadConfig.LocalConfig config) {
        this.config = config;
    }

    @Override
    public FileUploadInfo upload(InputStream inputStream, String path, String name) {
        FileUploadInfo info = new FileUploadInfo(FileLocationEnum.LOCAL);
        String key = path + File.separator + DateUtil.getMonthString() + File.separator + StringUtil.getUUID() + JOIN_STR + name;
        File file = FileUtil.writeFromStream(inputStream, config.getUploadPath() + File.separator + key);
        info.setPath(file.getAbsolutePath());
        return info;
    }

    @Override
    public void delete(String path) {
        FileUtil.del(path);
    }

    @Override
    public InputStream download(String path) {
        return FileUtil.getInputStream(path);
    }

}
