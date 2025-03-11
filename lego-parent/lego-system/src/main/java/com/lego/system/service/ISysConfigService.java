package com.lego.system.service;

import com.lego.system.dto.SysSystemInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISysConfigService {

    SysSystemInfo findInformation();

    List<String> findListBy(String code);

    String findValueBy(String code);

    void update(List<String> value, String code);

    void update(String value, String code);

    void importPackage(String operatorCode, MultipartFile file);
}
