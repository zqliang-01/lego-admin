package com.lego.system.action;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.system.dao.ISysAppSortDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysAppSort;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysPermission;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class ModifySysAppSortAction extends MaintainAction {

    private List<String> permissionCodes;

    private ISysAppSortDao sortDao = getDao(ISysAppSortDao.class);
    private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);
    private ISysPermissionDao permissionDao = getDao(ISysPermissionDao.class);

    public ModifySysAppSortAction(String operatorCode, List<String> permissionCodes) {
        super(SysPermissionCode.manage, operatorCode);
        this.permissionCodes = permissionCodes;
    }

    @Override
    protected void doRun() {
        List<SysAppSort> appSorts = sortDao.findByEmployee(operatorCode);
        sortDao.deleteAll(appSorts);
        if (CollectionUtil.isNotEmpty(permissionCodes)) {
            SysEmployee employee = employeeDao.findByCode(operatorCode);
            for (int i = 0; i < permissionCodes.size(); ++i) {
                String permissionCode = permissionCodes.get(i);
                SysPermission permission = permissionDao.findByCode(permissionCode);
                sortDao.save(new SysAppSort(i, permission, employee));
            }
        }
        this.description = "更新模型顺序";
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected void createLog() {
    }

    @Override
    protected String getEntityName() {
        return "APP排序";
    }
}
