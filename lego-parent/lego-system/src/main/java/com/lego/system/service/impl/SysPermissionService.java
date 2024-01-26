package com.lego.system.service.impl;

import com.lego.core.common.Constants;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.StringUtil;
import com.lego.system.action.AddSysPermissionAction;
import com.lego.system.action.DeleteSysPermissionAction;
import com.lego.system.action.ModifySysPermissionAction;
import com.lego.system.assembler.SysPermissionAssembler;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.simpletype.SysPermissionRouteType;
import com.lego.system.entity.simpletype.SysPermissionType;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPermissionCreateVO;
import com.lego.system.vo.SysPermissionModifyVO;
import com.lego.system.vo.SysPermissionTypeCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysPermissionService extends BusService<ISysPermissionDao, SysPermissionAssembler> implements ISysPermissionService {

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Override
    public void add(String employeeCode, SysPermissionCreateVO vo) {
        new AddSysPermissionAction(employeeCode, vo).run();
    }

    @Override
    public List<SysPermissionInfo> findBy(String routeType) {
        List<SysPermission> permissions = dao.findAll();
        List<SysPermissionInfo> infos = assembler.createTree(permissions);
        if (StringUtil.isBlank(routeType)) {
            return infos;
        }
        return infos.stream().filter(info -> routeType.equals(info.getRouteType().getCode())).collect(Collectors.toList());
    }

    @Override
    public List<SysPermissionInfo> findByEmployee(String employeeCode) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        if (employee.containRole(Constants.ADMIN_ROLE)) {
            return assembler.createTree(dao.findAll());
        }
        List<SysPermission> permissions = dao.findByEmployee(employeeCode);
        return assembler.createTree(permissions);
    }

    @Override
    public List<SysPermissionInfo> findDynamicByEmployee(String employeeCode) {
        List<SysPermissionInfo> infos = findByEmployee(employeeCode);
        return infos.stream().filter(info -> info.isDynamicRoute()).collect(Collectors.toList());
    }

    @Override
    public SysPermissionInfo findByCode(String code) {
        SysPermission permission = dao.findByCode(code);
        return assembler.create(permission);
    }

    @Override
    public List<SysPermissionInfo> findAllMenu() {
        List<SysPermission> permissions = dao.findByType(SysPermissionTypeCode.APP, SysPermissionTypeCode.MENU);
        permissions = permissions.stream().filter(p -> !p.getCode().startsWith(SysPermissionCode.manage)).collect(Collectors.toList());
        return assembler.createTree(permissions);
    }

    @Override
    public List<SysAppInfo> findAllApp() {
        List<SysPermission> permissions = dao.findByType(SysPermissionTypeCode.APP);
        return assembler.createApp(permissions);
    }

    @Override
    public List<SysAppInfo> findAppBy(String employeeCode) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        if (employee.containRole(Constants.ADMIN_ROLE)) {
            return findAllApp();
        }
        List<SysPermission> permissions = dao.findBy(employeeCode, SysPermissionTypeCode.APP);
        return assembler.createApp(permissions);
    }

    @Override
    public List<String> findSortAppBy(String employeeCode) {
        return dao.findSortCodeBy(employeeCode, SysPermissionTypeCode.APP);
    }

    @Override
    public List<String> findCodeBy(String roleCode) {
        if (Constants.ADMIN_ROLE.equals(roleCode)) {
            return dao.findAllCode();
        }
        return dao.findCodeByRole(roleCode);
    }

    @Override
    public int findMaxSn(String appCode) {
        return dao.findMaxSn(appCode);
    }

    @Override
    public List<TypeInfo> findAllType() {
        List<SysPermissionType> types = commonDao.findAll(SysPermissionType.class);
        return assembler.createTypeInfo(types);
    }

    @Override
    public List<TypeInfo> findAllRouteType() {
        List<SysPermissionRouteType> types = commonDao.findAll(SysPermissionRouteType.class);
        return assembler.createTypeInfo(types);
    }

    @Override
    public void modify(String loginCode, SysPermissionModifyVO vo) {
        new ModifySysPermissionAction(loginCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteSysPermissionAction(operatorCode, code).run();
    }

}
