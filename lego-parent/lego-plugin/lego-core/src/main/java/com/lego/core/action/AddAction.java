package com.lego.core.action;

import com.lego.core.enums.ActionType;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.vo.ReadableVO;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class AddAction<E extends BaseEntity, D extends IGenericDao<E>> extends EntityAction<E, D> {

    public AddAction(String permissionCode, String operatorCode) {
        super(permissionCode, operatorCode);
    }

    @Override
    protected void doRun() {
        setTargetEntity(createTargetEntity());
        ReadableVO snapshot = targetEntity.buildReadableSnapshot();
        this.description = addSnapshot(snapshot, targetEntity);
        entityDao.save(targetEntity);
        entityDao.flush();
    }

    protected final String addSnapshot(ReadableVO beforeSnapshot, BaseEntity domainObject) {
        ReadableVO afterSnapshot = domainObject.buildReadableSnapshot();
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
