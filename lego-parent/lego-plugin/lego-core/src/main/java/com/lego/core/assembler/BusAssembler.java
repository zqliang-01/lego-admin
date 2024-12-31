package com.lego.core.assembler;

import com.lego.core.data.hibernate.entity.BusEntity;
import com.lego.core.dto.BusDTO;

public abstract class BusAssembler<D extends BusDTO, E extends BusEntity> extends EntityAssembler<D, E> {

    public D create(E entity) {
        D dto = doCreate(entity);
        dto.setVersion(entity.getVersion());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreator(createEmployee(entity.getCreatorCode()));
        return dto;
    }

}
