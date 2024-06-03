package com.lego.core.web.upload;

import com.lego.core.dto.FileUploadInfo;

import java.io.InputStream;

public interface FileHandler {

    String JOIN_STR = "-";

    /**
     * 上传文件
     */
    FileUploadInfo upload(InputStream inputStream, String path, String name);

    /**
     * 删除文件
     */
    public void delete(String path);

    /**
     * 获取文件
     */
    public InputStream download(String path);
}
