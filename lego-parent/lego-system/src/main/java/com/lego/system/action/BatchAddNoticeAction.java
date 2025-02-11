package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysNotice;
import com.lego.system.entity.SysNoticeTemplate;
import com.lego.system.vo.SysPermissionCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BatchAddNoticeAction extends MaintainAction {

    private SysNoticeTemplate template;
    private ISysNoticeDao noticeDao = getDao(ISysNoticeDao.class);
    private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

    public BatchAddNoticeAction(String operatorCode, SysNoticeTemplate template) {
        super(SysPermissionCode.manage, operatorCode);
        this.template = template;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(template != null, "公告模板不能为空，公共发布失败！");
    }

    @Override
    protected void doRun() {
        Set<SysEmployee> employees = new HashSet<>(template.getEmployees());
        List<SysEmployee> deptEmployees = employeeDao.findBy(template.getDepts());
        employees.addAll(deptEmployees);

        List<SysNotice> notices = new ArrayList<>();
        SysEmployee creator = findByCode(SysEmployee.class, operatorCode);
        for (SysEmployee recipient : employees) {
            notices.add(new SysNotice(template, creator, recipient));
        }
        noticeDao.saveAll(notices);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    @Override
    protected void createLog() {
    }

    @Override
    protected String getEntityName() {
        return "批量公告";
    }
}
