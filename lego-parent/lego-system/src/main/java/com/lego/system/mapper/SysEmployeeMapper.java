package com.lego.system.mapper;

import java.util.List;

public interface SysEmployeeMapper {

    List<String> selectCodesByRoles(List<String> roleCodes);

    List<String> selectCodesByDepts(List<String> deptCodes);
}
