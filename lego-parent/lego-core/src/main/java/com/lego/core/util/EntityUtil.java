package com.lego.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.exception.CoreException;

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
