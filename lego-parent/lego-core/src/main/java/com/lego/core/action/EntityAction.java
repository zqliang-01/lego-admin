package com.lego.core.action;

import com.lego.core.data.ActionType;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.data.hibernate.BusEntity;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.exception.CoreException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class EntityAction<E extends BaseEntity, D extends IGenericDao<E>> extends MaintainAction {

    protected E targetEntity;
    protected D entityDao;

    protected EntityAction(String permissionCode, String operatorCode) {
        super(permissionCode, operatorCode);
        this.entityDao = getDao(getDaoClass());
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
            busEntity.setUpdateTime(DateUtil.currentDateTime());
        }
    }

    @Override
    protected String getEntityName() {
        return EntityUtil.getName(targetEntity);
    }
}
