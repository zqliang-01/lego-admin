package com.lego.core.data.hibernate.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.DTO;

public class BusiService<D extends IGenericDao<? extends BaseEntity>, A extends BaseAssembler<? extends DTO, ? extends BaseEntity>> extends BaseService {

    @Autowired
    protected D dao;

    @Autowired
    protected A assembler;

}
