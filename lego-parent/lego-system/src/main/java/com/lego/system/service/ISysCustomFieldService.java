package com.lego.system.service;

import com.lego.system.dto.SysCustomFieldInfo;

import java.util.List;

public interface ISysCustomFieldService {

    List<SysCustomFieldInfo> findBy(String formCode);

    List<SysCustomFieldInfo> findValidBy(String employeeCode, String formCode);

    List<String> findCodesByForm(String formCode);

    List<List<SysCustomFieldInfo>> findInit(String tableCode);
}
