package com.lego.core.web.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.lego.core.common.FileLocationEnum;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "lego.file")
public class UploadConfig {

	private String config;

    /**
     * oss配置
     */
    private OssConfig oss;

    /**
     * 本地配置
     */
    private LocalConfig local;

    @Bean
    public FileHandler fileHandler() {
    	if (FileLocationEnum.LOCAL.isTrue(config)) {
    		return new LocalFileHandler(this.local);
    	}
    	if (FileLocationEnum.ALI_OSS.isTrue(config)) {
    		return new AliOSSFileHandler(this.oss);
    	}
		return new LocalFileHandler(this.local);
    }

    @Data
    public static class OssConfig {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
        private String publicUrl;
    }

    @Data
    public static class LocalConfig {
        private String uploadPath;
    }

}
