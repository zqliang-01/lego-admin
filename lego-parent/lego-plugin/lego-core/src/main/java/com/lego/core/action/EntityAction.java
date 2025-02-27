package com.lego.core.action;

import com.lego.core.enums.ActionType;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.data.hibernate.entity.BusEntity;
import com.lego.core.exception.CoreException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class EntityAction<E extends BaseEntity, D extends IGenericDao<E>> extends MaintainAction {

    protected E targetEntity;
    protected D entityDao;
    private String checkStatus;

    protected EntityAction(String permissionCode, String operatorCode) {
        super(permissionCode, operatorCode);
        this.entityDao = getDao(getDaoClass());
    }

    protected EntityAction(String permissionCode, String operatorCode, String checkStatus) {
        this(permissionCode, operatorCode);
        this.checkStatus = checkStatus;
    }

    @SuppressWarnings("unchecked")
    private Class<D> getDaoClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type type = actualTypeArguments[1];
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getRawType();
        }
        return (Class<D>) type;
    }

    protected final void setTargetEntity(E targetObject) {
        CoreException.check(targetObject != null, "targetEntity is null");
        this.targetEntity = targetObject;
        if (targetEntity instanceof BusEntity) {
            BusEntity busEntity = (BusEntity) targetEntity;
            if (ActionType.ADD == getActionType()) {
                busEntity.setCreatorCode(operatorCode);
            }
            if (StringUtil.isNotBlank(checkStatus)) {
                busEntity.setCheckStatus(checkStatus);
            }
            busEntity.setUpdateTime(DateUtil.currentDateTime());
        }
    }

    protected boolean checkExists(String key, Object value) {
        GenericConditionVO condition = GenericConditionVO.create()
            .addItem(GenericConditionItemVO.createEqual(key, value));
        return entityDao.exists(condition);
    }

    protected boolean checkEntityExists(String key, Object value) {
        GenericConditionVO condition = GenericConditionVO.create()
            .addItem(GenericConditionItemVO.createEntityEqual(key, value));
        return entityDao.exists(condition);
    }

    @Override
    public String getEntityCode() {
        return EntityUtil.getCode(targetEntity);
    }

    @Override
    protected String getEntityName() {
        return EntityUtil.getName(targetEntity);
    }
}
