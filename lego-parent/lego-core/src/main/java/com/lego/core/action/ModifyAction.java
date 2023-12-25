package com.lego.core.action;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lego.core.data.ActionType;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;

public abstract class ModifyAction<T extends BaseEntity, D extends IGenericDao<T>> extends EntityAction<T, D> {

	private boolean checkDiff = true;

    public ModifyAction(String permissionCode, String operatorCode, String entityCode) {
        super(permissionCode, operatorCode);
        setTargetEntity(entityDao.findByCode(entityCode));
    }

    @Override
    protected void doRun() {
        BusinessException.check(targetEntity != null, "targetEntity未设置");
        Map<String, String> beforeSnapshot = targetEntity.buildReadableSnapshot();
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

    protected final String diffSnapshot(Map<String, String> beforeSnapshot, BaseEntity domainObject) {
        Map<String, String> afterSnapshot = domainObject.buildReadableSnapshot();

        StringBuilder sb = new StringBuilder();
        List<String> beforeClone = new ArrayList<String>(beforeSnapshot.keySet());
        beforeClone.retainAll(afterSnapshot.keySet());
        for (String key : beforeClone) {
            String beforeValue = beforeSnapshot.get(key);
            String afterValue = afterSnapshot.get(key);
            if (!StringUtil.equals(beforeValue, afterValue)) {
                sb.append(MessageFormat.format("{0}: 由\"{1}\"修改为\"{2}\"\r\n", key, beforeValue, afterValue));
            }
        }
        return sb.toString();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    protected void noCheckDiff() {
    	this.checkDiff = false;
    }

    protected abstract void doModify(T entity);
}
