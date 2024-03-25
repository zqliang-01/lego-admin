package com.lego.system.service.impl;

import com.lego.core.common.Constants;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.TypeInfo;
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
import com.lego.system.vo.SysPermissionCreateVO;
import com.lego.system.vo.SysPermissionModifyVO;
import com.lego.system.vo.SysPermissionTypeCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<SysPermission> permissions = dao.findByRouteType(routeType);
        return assembler.createTree(permissions);
    }

    @Override
    public List<SysPermissionInfo> findByEmployee(String employeeCode, String routeType) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        if (employee.isAdmin()) {
            return assembler.createTree(dao.findByRouteType(routeType));
        }
        List<SysPermission> permissions = dao.findByEmployee(employeeCode, routeType);
        return assembler.createTree(permissions);
    }

    @Override
    public SysPermissionInfo findByCode(String code) {
        SysPermission permission = dao.findByCode(code);
        return assembler.create(permission);
    }

    @Override
    public List<SysAppInfo> findAllApp() {
        List<SysPermission> permissions = dao.findByType(SysPermissionTypeCode.APP);
        return assembler.createApp(permissions);
    }

    @Override
    public List<SysAppInfo> findAppBy(String employeeCode) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        if (employee.isAdmin()) {
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
        if (Constants.ADMIN_ROLE_CODE.equals(roleCode)) {
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
