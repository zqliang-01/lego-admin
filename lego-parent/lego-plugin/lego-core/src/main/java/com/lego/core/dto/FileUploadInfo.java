package com.lego.core.dto;

import com.lego.core.common.FileLocationEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String path;
	private FileLocationEnum locationEnum;

	public FileUploadInfo(FileLocationEnum locationEnum) {
		this.locationEnum = locationEnum;
	}

	public String getLocationCode() {
		return locationEnum.getCode();
	}
}
