package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysPrintTemplate;

import java.util.List;

public interface ISysPrintTemplateDao extends IGenericDao<SysPrintTemplate> {

    List<SysPrintTemplate> findBy(String formCode);
}
