package com.lego.core.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.dto.TypeInfo;

@Component
public class TypeInfoAssembler {

	public TypeInfo create(BaseEntity entity) {
		if (entity == null) {
			return new TypeInfo();
		}
		return new TypeInfo(entity.getCode(), entity.getName());
	}

    public List<TypeInfo> create(Collection<? extends BaseEntity> entities) {
        List<TypeInfo> infos = new ArrayList<TypeInfo>();
        for (BaseEntity entity : entities) {
            infos.add(create(entity));
        }
        return infos;
    }

}
