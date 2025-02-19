package com.lego.core.data.hibernate.jpa;

import com.lego.core.data.hibernate.IBusGenericDao;
import com.lego.core.data.hibernate.entity.BusEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class BusGenericDao<T extends BusEntity> extends GenericDao<T> implements IBusGenericDao<T> {

}
