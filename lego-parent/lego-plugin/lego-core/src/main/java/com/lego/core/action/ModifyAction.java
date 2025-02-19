package com.lego.core.action;

import com.lego.core.enums.ActionType;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class ModifyAction<T extends BaseEntity, D extends IGenericDao<T>> extends EntityAction<T, D> {

    private boolean checkDiff = true;

    public ModifyAction(String permissionCode, String operatorCode, String entityCode) {
        super(permissionCode, operatorCode);
        setTargetEntity(entityDao.findByCode(entityCode));
    }

    @Override
    protected void doRun() {
        BusinessException.check(targetEntity != null, "targetEntity未设置");
        ReadableVO beforeSnapshot = targetEntity.buildReadableSnapshot();
        doModify(targetEntity);
        String diffSnapshot = diffSnapshot(beforeSnapshot, targetEntity);
        if (checkDiff) {
            BusinessException.check(StringUtil.isNotBlank(diffSnapshot), "内容无变化！");
        }
        this.description = diffSnapshot;
        if (StringUtil.isNotBlank(diffSnapshot)) {
            entityDao.save(targetEntity);
        }
    }

    protected final String diffSnapshot(ReadableVO beforeSnapshot, BaseEntity domainObject) {
        StringBuilder sb = new StringBuilder();
        ReadableVO afterSnapshot = domainObject.buildReadableSnapshot();
        List<String> beforeClone = new ArrayList<String>(beforeSnapshot.keySet());
        beforeClone.retainAll(afterSnapshot.keySet());
        for (String key : beforeClone) {
            String beforeValue = beforeSnapshot.getValue(key);
            String afterValue = afterSnapshot.getValue(key);
            if (!StringUtil.equals(beforeValue, afterValue)) {
                sb.append(MessageFormat.format("{0}: 由\"{1}\"修改为\"{2}\"\r\n", key, beforeSnapshot.get(key), afterSnapshot.get(key)));
            }
        }
        return sb.toString();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    protected void setCheckDiff(boolean checkDiff) {
        this.checkDiff = checkDiff;
    }

    protected abstract void doModify(T entity);
}
