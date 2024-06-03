package com.lego.core.action;

import com.lego.core.data.ActionType;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.exception.BusinessException;

public abstract class DeleteAction<E extends BaseEntity, D extends IGenericDao<E>> extends EntityAction<E, D> {

    public DeleteAction(String permissionCode, String operatorCode, String entityCode) {
        super(permissionCode, operatorCode);
        setTargetEntity(entityDao.findByCode(entityCode));
    }

    @Override
    protected void doRun() {
        BusinessException.check(targetEntity != null, "targetEntity未初始化");
        this.description = "删除记录 -> " + targetEntity.buildReadableSnapshotString();
        entityDao.delete(targetEntity);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.DELETE;
    }
}
