package com.lego.mobile.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.mobile.entity.MobileUserBind;

public interface IMobileUserBindDao extends IGenericDao<MobileUserBind> {

    MobileUserBind findBy(String openid, String type);

    MobileUserBind findByEmployee(String employee, String token);
}
