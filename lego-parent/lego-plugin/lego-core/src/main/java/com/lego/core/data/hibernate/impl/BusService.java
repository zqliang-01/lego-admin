package com.lego.core.data.hibernate.impl;

import com.lego.core.assembler.EntityAssembler;
import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.DTO;
import org.springframework.beans.factory.annotation.Autowired;

public class BusService<D extends IGenericDao<? extends BaseEntity>, A extends EntityAssembler<? extends DTO, ? extends BaseEntity>> extends BaseService {

    @Autowired
    protected D dao;

    @Autowired
    protected A assembler;

    public boolean exists(String code) {
        return dao.exists(code);
    }

}
