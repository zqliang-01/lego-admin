package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysSceneDao;
import com.lego.system.entity.SysScene;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysSceneAction extends DeleteAction<SysScene, ISysSceneDao> {

    public DeleteSysSceneAction(String operatorCode, String entityCode) {
        super(SysPermissionCode.manage, operatorCode, entityCode);
    }

    @Override
    protected void preprocess() {
        boolean isSaveScene = targetEntity.getEmployee().getCode().equals(operatorCode);
        BusinessException.check(isSaveScene, "场景归属人不一致，场景删除失败！");
    }

}
