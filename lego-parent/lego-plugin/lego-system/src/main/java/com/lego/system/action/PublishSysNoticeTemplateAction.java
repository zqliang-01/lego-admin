package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.util.DateUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysNoticeTemplateDao;
import com.lego.system.entity.SysNoticeTemplate;
import com.lego.system.vo.SysPermissionCode;

public class PublishSysNoticeTemplateAction extends ModifyAction<SysNoticeTemplate, ISysNoticeTemplateDao> {

    private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

    public PublishSysNoticeTemplateAction(String operatorCode, String code) {
        super(SysPermissionCode.manage, operatorCode, code);
    }

    @Override
    protected void doModify(SysNoticeTemplate entity) {
        entity.setPublished(true);
        entity.setPublishedTime(DateUtil.currentDateTime());
    }

    @Override
    protected void postprocess() {
        new BatchAddNoticeAction(operatorCode, targetEntity).run();
    }
}
