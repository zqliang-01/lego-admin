package com.lego.core.enums;

import com.lego.core.util.StringUtil;

import lombok.Getter;

@Getter
public enum FileLocationEnum {

    /**
     * 本地上传
     */
    LOCAL("local", "本地"),
    /**
     * 阿里云OSS
     */
    ALI_OSS("aliOss", "阿里云OSS");

	private String code;
	private String name;

	private FileLocationEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public boolean isTrue(String code) {
		return StringUtil.equals(this.code, code);
	}
}
