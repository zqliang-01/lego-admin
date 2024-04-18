package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.system.entity.SysNotice;
import com.lego.system.vo.SysNoticeSearchVO;

import java.util.List;

public interface ISysNoticeDao extends IGenericDao<SysNotice> {

    List<SysNotice> findUnReadBy(String recipientCode);

    List<SysNotice> findBy(String recipientCode, boolean readed);

    LegoPage<SysNotice> findPageBy(String operatorCode, SysNoticeSearchVO vo);

    long findUnreadCount(String operatorCode);
}
