package com.lego.core.assembler;

import com.lego.core.data.hibernate.BusEntity;
import com.lego.core.data.hibernate.ICommonService;
import com.lego.core.dto.BusDTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.core.web.LegoBeanFactory;

public abstract class BusAssembler<D extends BusDTO, E extends BusEntity> extends EntityAssembler<D, E> {

    public D create(E entity) {
        D dto = doCreate(entity);
        dto.setVersion(entity.getVersion());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        ICommonService actionService = LegoBeanFactory.getBeanWithNull(ICommonService.class);
        if (actionService != null) {
            dto.setCreator(actionService.findEmployeeBy(entity.getCreatorCode()));
        }
        return dto;
    }

    protected TypeInfo create(CustomFieldTypeEnum type, String code) {
        ICommonService actionService = LegoBeanFactory.getBeanWithNull(ICommonService.class);
        if (actionService == null) {
            return TypeInfo.NULL;
        }
        if (type == CustomFieldTypeEnum.EMPLOYEE) {
            return actionService.findEmployeeBy(code);
        }
        if (type == CustomFieldTypeEnum.DEPT) {
            return actionService.findDeptBy(code);
        }
        return TypeInfo.NULL;
    }
}
