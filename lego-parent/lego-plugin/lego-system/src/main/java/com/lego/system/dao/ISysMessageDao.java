package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.system.entity.SysMessage;
import com.lego.system.vo.SysMessageSearchVO;

import java.util.List;

public interface ISysMessageDao extends IGenericDao<SysMessage> {

    List<SysMessage> findUnReadBy(String recipientCode, String type);

    List<SysMessage> findBy(String recipientCode, String type, boolean readed);

    LegoPage<SysMessage> findPageBy(String operatorCode, SysMessageSearchVO vo);

    SysMessage findByCode(String operatorCode, String code);
}
