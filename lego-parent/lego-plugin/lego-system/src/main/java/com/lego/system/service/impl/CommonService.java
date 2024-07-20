package com.lego.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.common.Constants;
import com.lego.core.data.ICommonService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.core.vo.ActionVO;
import com.lego.core.vo.SysMessageCreateVO;
import com.lego.system.action.AddSysMessageAction;
import com.lego.system.assembler.SysDeptAssembler;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysOperationLogDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysDept;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysOperationLog;
import com.lego.system.entity.SysPermission;
import com.lego.system.mapper.SysEmployeeMapper;
import com.lego.system.mapper.SysPermissionMapper;
import com.lego.system.mapper.SysRoleMapper;
import com.lego.system.vo.DataScopeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class CommonService implements ICommonService {

    @Resource
    private ISysOperationLogDao operationDao;

    @Resource
    private ISysEmployeeDao employeeDao;

    @Resource
    private ISysDeptDao deptDao;

    @Resource
    private ISysPermissionDao permissionDao;

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysEmployeeMapper employeeMapper;

    @Autowired
    private SysDeptAssembler deptAssembler;

    @Override
    public void addLog(ActionVO vo) {
        SysEmployee operator = employeeDao.findByCode(vo.getOperatorCode());
        SysPermission permission = permissionDao.findByCode(vo.getPermissionCode());
        SysOperationLog sysOperationLog = new SysOperationLog(vo.getActionType(), operator, permission);
        sysOperationLog.setEntityCode(vo.getEntityCode());
        sysOperationLog.setEntityName(vo.getEntityName());
        sysOperationLog.setDescription(vo.getDescription());
        operationDao.save(sysOperationLog);
    }

    @Override
    public TypeInfo findEmployeeBy(String code) {
        SysEmployee employee = employeeDao.findByUnsureCode(code);
        return EntityUtil.toTypeInfo(employee);
    }

    @Override
    public TypeInfo findDeptBy(String code) {
        SysDept dept = deptDao.findByUnsureCode(code);
        return EntityUtil.toTypeInfo(dept);
    }

    @Override
    public void addSysMessage(SysMessageCreateVO vo) {
        new AddSysMessageAction(vo.getCreator(), vo).run();
    }

    @Override
    public String findReportCodeBy(String permissionCode) {
        SysPermission permission = permissionDao.findByCode(permissionCode);
        return permission.getReportCode();
    }

    @Override
    public List<String> findPermissionCodesBy(String employeeCode) {
        return permissionMapper.selectCodesByEmployee(employeeCode);
    }

    @Override
    public List<String> findRoleCodesBy(String employeeCode) {
        return roleMapper.selectCodesByEmployee(employeeCode);
    }

    @Override
    public List<String> findDataPermissionEmployeeCode() {
        SysEmployee employee = employeeDao.findByCode(Constants.loginCode.get());
        DataScopeType dataScope = employee.getDataScope();
        if (dataScope == DataScopeType.ALL || employee.isAdmin()) {
            return CollectionUtil.newArrayList();
        }
        if (dataScope == DataScopeType.DEPT) {
            List<String> deptCodes = Arrays.asList(employee.getDept().getCode());
            return CollectionUtil.newArrayList(employeeMapper.selectCodesByDepts(deptCodes));
        }
        if (dataScope == DataScopeType.DEPT_AND_CHILD) {
            List<String> deptCodes = deptAssembler.createChildrenCodes(employee.getDept());
            return CollectionUtil.newArrayList(employeeMapper.selectCodesByDepts(deptCodes));
        }
        return CollectionUtil.newArrayList(employee.getCode());
    }
}