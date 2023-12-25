package com.lego.system.service;

import java.util.List;

import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysCustomFieldInfo;
import com.lego.system.dto.SysTableColumnInfo;

public interface ISysCustomFieldService {

	List<SysCustomFieldInfo> findBy(String formCode);

	List<TypeInfo> findGenColumnBy(String tableCode);

	SysTableColumnInfo findValidBy(String employeeCode, String formCode);

	List<String> findCodesByPermission(String permissionCode);

	List<List<SysCustomFieldInfo>> findInit(String appCode, String tableCode);
}
