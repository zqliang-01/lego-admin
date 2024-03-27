package com.lego.system.service;

import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.vo.SysPermissionCreateVO;
import com.lego.system.vo.SysPermissionModifyVO;

import java.util.List;

public interface ISysPermissionService {

    void add(String employeeCode, SysPermissionCreateVO vo);

    List<SysPermissionInfo> findBy(String routeType);

    List<SysPermissionInfo> findByEmployee(String employeeCode, String... types);

    SysPermissionInfo findByCode(String code);

    List<SysAppInfo> findAllApp();

    List<SysAppInfo> findAppBy(String employeeCode);

    List<String> findSortAppBy(String employeeCode);

    List<String> findCodeBy(String roleCode);

    int findMaxSn(String appCode);

    List<TypeInfo> findAllType();

    List<TypeInfo> findAllRouteType();

    void modify(String loginCode, SysPermissionModifyVO vo);

    void delete(String operatorCode, String code);
}
