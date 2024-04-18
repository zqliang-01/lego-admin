package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysNoticeTemplateDao;
import com.lego.system.entity.SysDept;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysNoticeTemplate;
import com.lego.system.vo.SysNoticeTemplateModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysNoticeTemplateAction extends ModifyAction<SysNoticeTemplate, ISysNoticeTemplateDao> {

    private SysNoticeTemplateModifyVO vo;

    public ModifySysNoticeTemplateAction(String operatorCode, SysNoticeTemplateModifyVO vo) {
        super(SysPermissionCode.manage, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(!targetEntity.isPublished(), "通知[{0}]已发布，修改失败！", targetEntity.getName());
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "公告标题不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getContent()), "公告内容不能为空！");
    }

    @Override
    protected void doModify(SysNoticeTemplate entity) {
        entity.setName(vo.getName());
        entity.setContent(vo.getContent());
        entity.setEmployees(findByCodes(SysEmployee.class, vo.getEmployees()));
        entity.setDepts(findByCodes(SysDept.class, vo.getDepts()));
    }
}
