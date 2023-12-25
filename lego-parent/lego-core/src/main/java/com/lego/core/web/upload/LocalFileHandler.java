package com.lego.core.web.upload;

import java.io.File;
import java.io.InputStream;

import com.lego.core.common.FileLocationEnum;
import com.lego.core.dto.FileUploadInfo;
import com.lego.core.util.DateUtil;

import cn.hutool.core.io.FileUtil;

public class LocalFileHandler implements FileHandler{

    private UploadConfig.LocalConfig config;

    public LocalFileHandler(UploadConfig.LocalConfig config) {
    	this.config = config;
	}

	@Override
	public FileUploadInfo upload(InputStream inputStream, String code, String name) {
		FileUploadInfo info = new FileUploadInfo(FileLocationEnum.LOCAL);
		String key = DateUtil.getMonthString() + "/" + code + JOIN_STR + name;
        File file = FileUtil.writeFromStream(inputStream, config.getUploadPath() + "/" + key);
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
