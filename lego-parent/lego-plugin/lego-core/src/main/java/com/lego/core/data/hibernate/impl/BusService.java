package com.lego.core.data.hibernate.impl;

import com.lego.core.assembler.BusAssembler;
import com.lego.core.data.hibernate.IBusGenericDao;
import com.lego.core.data.hibernate.IBusService;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.data.hibernate.entity.BusEntity;
import com.lego.core.dto.DTO;

public abstract class BusService<D extends IBusGenericDao<? extends BusEntity>, A extends BusAssembler<? extends DTO, ? extends BusEntity>> extends BaseService<D, A> implements IBusService {

    @Override
    public void updateCheckStatus(String code, String checkStatus) {
        BusEntity entity = dao.findByCode(code);
        entity.updateCheckStatus(checkStatus);
        ((IGenericDao) dao).save(entity);
    }

    @Override
    public boolean exists(String code) {
        return dao.exists(code);
    }
}
