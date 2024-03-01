package com.lego.core.action;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lego.core.data.ActionType;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.data.hibernate.IGenericDao;

public abstract class AddAction<E extends BaseEntity, D extends IGenericDao<E>> extends EntityAction<E, D> {

    public AddAction(String permissionCode, String operatorCode) {
        super(permissionCode, operatorCode);
    }

    @Override
    protected void doRun() {
        setTargetEntity(createTargetEntity());
        Map<String, String> snapshot = targetEntity.buildReadableSnapshot();
        this.description = addSnapshot(snapshot, targetEntity);
        entityDao.save(targetEntity);
        entityDao.flush();
    }

    protected final String addSnapshot(Map<String, String> beforeSnapshot, BaseEntity domainObject) {
        Map<String, String> afterSnapshot = domainObject.buildReadableSnapshot();
        StringBuilder sb = new StringBuilder("新增记录 ->");
        List<String> beforeClone = new ArrayList<String>(beforeSnapshot.keySet());
        beforeClone.retainAll(afterSnapshot.keySet());
        for (String key : beforeClone) {
            String value = beforeSnapshot.get(key);
            sb.append(MessageFormat.format("{0}: \"{1}\" \r\n", key, value));
        }
        return sb.toString();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    protected abstract E createTargetEntity();
}
