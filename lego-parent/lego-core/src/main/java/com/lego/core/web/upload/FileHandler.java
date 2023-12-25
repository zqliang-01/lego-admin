package com.lego.core.web.upload;

import java.io.InputStream;

import com.lego.core.dto.FileUploadInfo;

public interface FileHandler {

	String JOIN_STR = "-";
    /**
     * 上传文件
     */
	FileUploadInfo upload(InputStream inputStream, String code, String name);

    /**
     * 删除文件
     */
    public void delete(String path);

    /**
     * 获取文件
     */
    public InputStream download(String path);
}
