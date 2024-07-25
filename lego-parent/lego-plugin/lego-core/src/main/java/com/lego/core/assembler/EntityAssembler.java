package com.lego.core.assembler;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.VersionDTO;
import com.lego.core.util.EntityUtil;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityAssembler<D extends DTO, E extends BaseEntity> extends BaseAssembler<D, E> {

    public List<String> createCodes(List<E> entities) {
        return entities.stream().map(BaseEntity::getCode).collect(Collectors.toList());
    }

    public D create(E entity) {
        D dto = doCreate(entity);
        if (dto instanceof VersionDTO) {
            VersionDTO versionDTO = (VersionDTO) dto;
            versionDTO.setVersion(entity.getVersion());
        }
        return dto;
    }

    public TypeInfo createTypeInfo(BaseEntity entity) {
        return EntityUtil.toTypeInfo(entity);
    }

    public List<TypeInfo> createTypeInfo(Collection<? extends BaseEntity> entities) {
        return EntityUtil.toTypeInfo(entities);
    }

}
