package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysMessageDao;
import com.lego.system.entity.SysMessage;
import com.lego.system.vo.SysMessageSearchVO;

import java.util.List;

public class SysMessageDao extends GenericDao<SysMessage> implements ISysMessageDao {

    @Override
    public List<SysMessage> findUnReadBy(String recipientCode, String type) {
        QueryHandler<SysMessage> query = createQueryHandler();
        query.condition("t.readed = :readed").param("readed", false);
        query.condition("t.recipient.code = :recipientCode").param("recipientCode", recipientCode);
        if (StringUtil.isNotBlank(type)) {
            query.condition("t.type.code = :typeCode").param("typeCode", type);
        }
        return query.findList();
    }

    @Override
    public List<SysMessage> findBy(String recipientCode, String type, boolean readed) {
        QueryHandler<SysMessage> query = createQueryHandler();
        query.condition("t.readed = :readed").param("readed", readed);
        query.condition("t.recipient.code = :recipientCode").param("recipientCode", recipientCode);
        if (StringUtil.isNotBlank(type)) {
            query.condition("t.type.code = :typeCode").param("typeCode", type);
        }
        return query.findList();
    }

    @Override
    public LegoPage<SysMessage> findPageBy(String operatorCode, SysMessageSearchVO vo) {
        QueryHandler<SysMessage> query = createQueryHandler();
        query.condition("t.recipient.code = :recipientCode").param("recipientCode", operatorCode);
        if (StringUtil.isNotBlank(vo.getType())) {
            query.condition("t.type.code = :typeCode").param("typeCode", vo.getType());
        }
        if (vo.getReaded() != null) {
            query.condition("t.readed = :readed").param("readed", vo.getReaded());
        }
        query.order("t.createTime DESC");
        return query.findPage(vo);
    }
}
