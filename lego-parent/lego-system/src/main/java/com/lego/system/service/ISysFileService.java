package com.lego.system.service;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.lego.system.dto.SysFileInfo;

public interface ISysFileService {

	SysFileInfo findBy(String code);

	List<SysFileInfo> findBy(String entityCode, String permissionCode);

	String upload(String operatorCode, MultipartFile file, String entityCode, String permissionCode);

	void delete(String operatorCode, String permissionCode, String code);

	void download(HttpServletResponse response, String code);

	void modify(String loginCode, String permissionCode, String code, String name);
}
