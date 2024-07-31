package com.lego.system.action;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.action.DeleteAction;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class DeleteSysCustomFieldAction extends DeleteAction<SysCustomField, ISysCustomFieldDao> {

    private ISysColumnSortDao sortDao = getDao(ISysColumnSortDao.class);

    public DeleteSysCustomFieldAction(String operatorCode, String fieldCode) {
        super(SysPermissionCode.manageCustomForm, operatorCode, fieldCode);
    }

    @Override
    protected void preprocess() {
        List<SysColumnSort> sorts = sortDao.findByField(targetEntity);
        if (CollectionUtil.isNotEmpty(sorts)) {
            sortDao.deleteAll(sorts);
        }
    }

    @Override
    protected void createLog() {
    }
}
