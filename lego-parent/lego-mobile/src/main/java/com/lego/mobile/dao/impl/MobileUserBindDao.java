package com.lego.mobile.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.mobile.dao.IMobileUserBindDao;
import com.lego.mobile.entity.MobileUserBind;

public class MobileUserBindDao extends GenericDao<MobileUserBind> implements IMobileUserBindDao {

    @Override
    public MobileUserBind findBy(String openid, String type) {
        QueryHandler<MobileUserBind> query = createQueryHandler();
        query.condition("t.openid = :openid").param("openid", openid);
        query.condition("t.type.code = :type").param("type", type);
        return query.findUnique();
    }

    @Override
    public MobileUserBind findByEmployee(String employee, String token) {
        QueryHandler<MobileUserBind> query = createQueryHandler();
        query.condition("t.employeeCode = :employeeCode").param("employeeCode", employee);
        query.condition("t.token = :token").param("token", token);
        return query.findUnique();
    }
}
