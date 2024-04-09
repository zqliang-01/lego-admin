package com.lego.system.service.impl;

import com.lego.core.data.hibernate.ICommonService;
import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.ActionVO;
import com.lego.core.vo.SysMessageCreateVO;
import com.lego.system.action.AddSysMessageAction;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysOperationLogDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysDept;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysOperationLog;
import com.lego.system.entity.SysPermission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommonService extends BaseService implements ICommonService {

    @Resource
    private ISysOperationLogDao operationDao;

    @Resource
    private ISysEmployeeDao employeeDao;

    @Resource
    private ISysDeptDao deptDao;

    @Resource
    private ISysPermissionDao permissionDao;

    @Override
    public void save(ActionVO vo) {
        SysEmployee operator = employeeDao.findByCode(vo.getOperatorCode());
        SysPermission permission = permissionDao.findByCode(vo.getPermissionCode());
        SysOperationLog sysOperationLog = new SysOperationLog(vo.getEntityCode(), vo.getActionType(), operator, permission);
        sysOperationLog.setDescription(vo.getDescription());
        operationDao.save(sysOperationLog);
    }

    @Override
    public TypeInfo findEmployeeBy(String code) {
        SysEmployee employee = employeeDao.findByUnsureCode(code);
        return typeInfoAssembler.create(employee);
    }

    @Override
    public TypeInfo findDeptBy(String code) {
        SysDept dept = deptDao.findByUnsureCode(code);
        return typeInfoAssembler.create(dept);
    }

    @Override
    public void addSysMessage(String operatorCode, SysMessageCreateVO vo) {
        new AddSysMessageAction(operatorCode, vo).run();
    }
}