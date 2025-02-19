package com.lego.core.util;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.data.hibernate.entity.SimpleType;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.CoreException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityUtil {

    public static String getCode(BaseEntity entity) {
        if (entity == null) {
            return "";
        }
        return entity.getCode();
    }

    public static List<String> getCode(List<? extends BaseEntity> entities) {
        List<String> codes = new ArrayList<String>();
        for (BaseEntity entity : entities) {
            codes.add(entity.getCode());
        }
        return codes;
    }

    public static String getName(BaseEntity entity) {
        if (entity == null) {
            return "";
        }
        return entity.getName();
    }

    public static String toString(BaseEntity entity) {
        if (entity == null) {
            return "";
        }
        return entity.getName() + "(" + entity.getCode() + ")";
    }

    public static TypeInfo toTypeInfo(BaseEntity entity) {
        if (entity == null) {
            return TypeInfo.NULL;
        }
        return new TypeInfo(entity.getCode(), entity.getName());
    }

    public static List<TypeInfo> toTypeInfo(Collection<? extends BaseEntity> entities) {
        List<TypeInfo> infos = new ArrayList<TypeInfo>();
        for (BaseEntity entity : entities) {
            infos.add(toTypeInfo(entity));
        }
        return infos;
    }

    public static List<TypeInfo> toSimpleTypeInfo(Collection<? extends SimpleType> entities) {
        List<TypeInfo> infos = new ArrayList<TypeInfo>();
        for (SimpleType entity : entities) {
            infos.add(new TypeInfo(entity.getCode(), entity.getName(), entity.getSerialNumber()));
        }
        return infos;
    }

    public static String toString(Collection<? extends BaseEntity> entities) {
        if (entities == null) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        for (BaseEntity entity : entities) {
            sb.append(toString(entity));
        }
        return sb.toString();
    }

    public static <T> T uniqueOrNull(List<T> objects, Class<T> domainClass) {
        int size = objects.size();
        if (size == 0) {
            return null;
        }
        CoreException.check(size == 1, "[{0}]最多只能返回一条记录，实际返回[{1}]", domainClass.getSimpleName(), size);
        return objects.get(0);
    }
}
