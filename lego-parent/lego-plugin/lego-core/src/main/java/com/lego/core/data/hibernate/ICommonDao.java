package com.lego.core.data.hibernate;

import com.lego.core.data.hibernate.entity.BaseEntity;

import java.util.Date;
import java.util.List;

public interface ICommonDao {

    Date findCurrentDate();

    <T extends BaseEntity> T findByCode(Class<T> clazz, String code);

    <T extends BaseEntity> T findByUnsureCode(Class<T> clazz, String code);

    <T extends BaseEntity> List<T> findByCodes(Class<T> clazz, List<String> codes);

    <T extends BaseEntity> List<T> findAll(Class<T> clazz);
}
