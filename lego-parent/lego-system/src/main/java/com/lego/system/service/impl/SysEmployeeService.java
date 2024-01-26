package com.lego.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.system.action.AddSysEmployeeAction;
import com.lego.system.action.LoginSysEmployeeAction;
import com.lego.system.action.ModifySysEmployeeAction;
import com.lego.system.action.ModifySysEmployeeCurrentAction;
import com.lego.system.action.ModifySysEmployeePasswordAction;
import com.lego.system.action.ModifySysEmployeeRoleAction;
import com.lego.system.action.ResetPasswordAction;
import com.lego.system.action.UpdatePasswordAction;
import com.lego.system.assembler.SysEmployeeAssembler;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dto.SysEmployeeInfo;
import com.lego.system.dto.SysLoginInfo;
import com.lego.system.entity.SysEmployee;
import com.lego.system.service.ISysEmployeeService;
import com.lego.system.vo.SysEmployeeCreateVO;
import com.lego.system.vo.SysEmployeeCurrentModifyVO;
import com.lego.system.vo.SysEmployeeModifyVO;
import com.lego.system.vo.SysEmployeePasswordModifyVO;
import com.lego.system.vo.SysEmployeeRoleModifyVO;
import com.lego.system.vo.SysEmployeeSearchVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysEmployeeService extends BusService<ISysEmployeeDao, SysEmployeeAssembler> implements ISysEmployeeService {

    @Override
    public LegoPage<SysEmployeeInfo> findBy(SysEmployeeSearchVO vo) {
        return assembler.create(dao.findBy(vo));
    }

    @Override
    public void add(String operatorCode, SysEmployeeCreateVO vo) {
        new AddSysEmployeeAction(operatorCode, vo).run();
    }

    @Override
    public void modify(String operatorCode, SysEmployeeModifyVO vo) {
        new ModifySysEmployeeAction(operatorCode, vo).run();
    }

    @Override
    public void modifyCurrent(String operatorCode, SysEmployeeCurrentModifyVO vo) {
        new ModifySysEmployeeCurrentAction(operatorCode, vo).run();
    }

    @Override
    public void modifyRole(String operatorCode, SysEmployeeRoleModifyVO vo) {
        new ModifySysEmployeeRoleAction(operatorCode, vo).run();
    }

    @Override
    public void modifyPassword(String operatorCode, SysEmployeePasswordModifyVO vo) {
        new ModifySysEmployeePasswordAction(operatorCode, vo).run();
    }

    @Override
    public SysLoginInfo login(String code, String password) {
        new LoginSysEmployeeAction(code, password).run();
        return new SysLoginInfo(code, StpUtil.getTokenValue());
    }

    @Override
    public SysEmployeeInfo findDTOByCode(String code) {
        SysEmployee employee = dao.findByCode(code);
        return assembler.create(employee);
    }

    @Override
    public TypeInfo findSimpleTypeBy(String code) {
        SysEmployee employee = dao.findByCode(code);
        return assembler.createTypeInfo(employee);
    }

    @Override
    public void updatePassword(String employeeCode, String oldPassword, String newPassword) {
        new UpdatePasswordAction(employeeCode, oldPassword, newPassword).run();
    }

    @Override
    public List<TypeInfo> findSimpleType() {
        return assembler.createTypeInfo(dao.findAll());
    }

    @Override
    public void resetPassword(String employeeCode, List<String> codes) {
        new ResetPasswordAction(employeeCode, codes).run();
    }

}
