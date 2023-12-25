package com.lego.system.mapper;

import java.util.List;

public interface SysRoleMapper {

	List<String> selectCodesByEmployee(String employeeCode);
}
