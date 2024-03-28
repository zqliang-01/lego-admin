package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.system.dao.ISysSceneDao;
import com.lego.system.entity.SysScene;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysSceneAction extends DeleteAction<SysScene, ISysSceneDao> {

    public DeleteSysSceneAction(String operatorCode, String entityCode) {
        super(SysPermissionCode.manage, operatorCode, entityCode);
    }

}
