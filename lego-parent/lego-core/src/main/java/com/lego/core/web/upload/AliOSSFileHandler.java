package com.lego.core.web.upload;

import java.io.InputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.lego.core.common.FileLocationEnum;
import com.lego.core.dto.FileUploadInfo;
import com.lego.core.util.DateUtil;

public class AliOSSFileHandler implements FileHandler{

    private OSS client;
    private UploadConfig.OssConfig config;

	public AliOSSFileHandler(UploadConfig.OssConfig config) {
		this.config = config;
        this.client = new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
	}

	@Override
	public FileUploadInfo upload(InputStream inputStream, String code, String name) {
		FileUploadInfo info = new FileUploadInfo(FileLocationEnum.ALI_OSS);
        String key = DateUtil.getMonthString() + "/" + code + JOIN_STR + name;
        info.setPath(key);
        try {
            client.putObject(config.getBucketName(), key, inputStream);
        }
        finally {
            client.shutdown();
        }
        return info;
    }

	@Override
	public void delete(String path) {
        try {
        	client.deleteObject(config.getBucketName(), path);
        }
        finally {
        	client.shutdown();
        }
	}

	@Override
	public InputStream download(String path) {
        try {
            OSSObject object = client.getObject(config.getBucketName(), path);
            return object.getObjectContent();
        }
        finally {
        	client.shutdown();
        }
    }

}
