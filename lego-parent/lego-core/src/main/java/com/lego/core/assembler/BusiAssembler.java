package com.lego.core.assembler;

import com.lego.core.data.hibernate.BusiEntity;
import com.lego.core.data.hibernate.ICommonService;
import com.lego.core.dto.BusiDTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.core.web.LegoBeanFactory;

public abstract class BusiAssembler<D extends BusiDTO, E extends BusiEntity> extends BaseAssembler<D, E> {

	@Override
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
		if (actionService  == null) {
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
