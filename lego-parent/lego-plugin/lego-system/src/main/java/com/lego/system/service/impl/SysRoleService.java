package com.lego.system.service.impl;

import com.lego.core.common.Constants;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.action.AddSysRoleAction;
import com.lego.system.action.AuthRoleAction;
import com.lego.system.action.AuthRoleDataScopeAction;
import com.lego.system.action.ModifySysRoleAction;
import com.lego.system.assembler.SysRoleAssembler;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.dto.SysRoleInfo;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysRole;
import com.lego.system.service.ISysRoleService;
import com.lego.system.vo.SysDataScopeAuthVO;
import com.lego.system.vo.SysPermissionAuthVO;
import com.lego.system.vo.SysRoleCreateVO;
import com.lego.system.vo.SysRoleModifyVO;
import com.lego.system.vo.SysRoleSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleService extends BusService<ISysRoleDao, SysRoleAssembler> implements ISysRoleService {

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Override
    public void add(String operatorCode, SysRoleCreateVO vo) {
        new AddSysRoleAction(operatorCode, vo).run();
    }

    @Override
    public void modify(String operatorCode, SysRoleModifyVO vo) {
        new ModifySysRoleAction(operatorCode, vo).run();
    }

    @Override
    public void auth(String operatorCode, SysPermissionAuthVO vo) {
        BusinessException.check(StringUtil.isNotBlank(vo.getRoleCode()), "请选择授权角色！");
        new AuthRoleAction(operatorCode, vo).run();
    }

    @Override
    public void authDataScope(String operatorCode, SysDataScopeAuthVO vo) {
        new AuthRoleDataScopeAction(operatorCode, vo).run();
    }

    @Override
    public List<SysRoleInfo> findBy(SysRoleSearchVO vo) {
        List<SysRole> roles = dao.findBy(vo);
        return assembler.create(roles);
    }

    @Override
    public SysRoleInfo findByCode(String code) {
        SysRole role = dao.findByCode(code);
        return assembler.create(role);
    }

    @Override
    public void deleteBy(String code) {
        BusinessException.check(!Constants.ADMIN_ROLE_CODE.equals(code), "不允许删除超级管理员角色！");
        SysRole role = dao.findByCode(code);
        dao.delete(role);
    }

    @Override
    public List<TypeInfo> findSimpleType(String name, String operatorCode) {
        SysEmployee employee = employeeDao.findByCode(operatorCode);
        List<SysRole> roles = dao.findBy(name);
        if (employee.isAdmin()) {
            return assembler.createTypeInfo(roles);
        }
        List<SysRole> filterRoles = roles.stream()
            .filter(role -> !Constants.ADMIN_ROLE_CODE.equals(role.getCode()))
            .collect(Collectors.toList());
        return assembler.createTypeInfo(filterRoles);
    }

}
