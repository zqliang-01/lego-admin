package com.lego.core.data.hibernate;

import java.util.Date;
import java.util.List;

public interface ICommonDao {

    Date findCurrentDate();

    <T extends BaseEntity> T findByCode(Class<T> clazz, String code);

    <T extends BaseEntity> T findByUnsureCode(Class<T> clazz, String code);

    <T extends BaseEntity> List<T> findAll(Class<T> clazz);
}
