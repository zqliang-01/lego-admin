package com.lego.system.mapper;

import java.util.List;

public interface SysPermissionMapper {

	List<String> selectCodesByEmployee(String employeeCode);
}
