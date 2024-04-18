package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.entity.SysNotice;
import com.lego.system.vo.SysNoticeSearchVO;

import java.util.List;

public class SysNoticeDao extends GenericDao<SysNotice> implements ISysNoticeDao {

    @Override
    public List<SysNotice> findUnReadBy(String recipientCode) {
        QueryHandler<SysNotice> query = createQueryHandler();
        query.condition("t.readed = :readed").param("readed", false);
        query.condition("t.recipient.code = :recipientCode").param("recipientCode", recipientCode);
        return query.findList();
    }

    @Override
    public List<SysNotice> findBy(String recipientCode, boolean readed) {
        QueryHandler<SysNotice> query = createQueryHandler();
        query.condition("t.readed = :readed").param("readed", readed);
        query.condition("t.recipient.code = :recipientCode").param("recipientCode", recipientCode);
        return query.findList();
    }

    @Override
    public LegoPage<SysNotice> findPageBy(String operatorCode, SysNoticeSearchVO vo) {
        QueryHandler<SysNotice> query = createQueryHandler();
        query.condition("t.recipient.code = :recipientCode").param("recipientCode", operatorCode);
        if (vo.getReaded() != null) {
            query.condition("t.readed = :readed").param("readed", vo.getReaded());
        }
        query.order("t.createTime DESC");
        return query.findPage(vo);
    }

    @Override
    public long findUnreadCount(String operatorCode) {
        QueryHandler<SysNotice> query = createQueryHandler();
        query.condition("t.readed = :readed").param("readed", false);
        query.condition("t.recipient.code = :recipientCode").param("recipientCode", operatorCode);
        return query.findCount();
    }
}
