package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.LegoPage;
import com.lego.system.action.DeleteAllSysMessageAction;
import com.lego.system.action.DeleteSysMessageAction;
import com.lego.system.action.ReadAllSysMessageAction;
import com.lego.system.action.ReadSysMessageAction;
import com.lego.system.assembler.SysMessageAssembler;
import com.lego.system.dao.ISysMessageDao;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.dto.SysMessageCountInfo;
import com.lego.system.dto.SysMessageInfo;
import com.lego.system.mapper.SysMessageMapper;
import com.lego.system.service.ISysMessageService;
import com.lego.system.vo.SysMessageSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysMessageService extends BaseService<ISysMessageDao, SysMessageAssembler> implements ISysMessageService {

    @Autowired
    private SysMessageMapper messageMapper;

    @Autowired
    private ISysNoticeDao noticeDao;

    @Override
    public void read(String operatorCode, String code) {
        new ReadSysMessageAction(operatorCode, code).run();
    }

    @Override
    public void readAll(String operatorCode, String type) {
        new ReadAllSysMessageAction(operatorCode, type).run();
    }

    @Override
    public LegoPage<SysMessageInfo> findBy(String operatorCode, SysMessageSearchVO vo) {
        return assembler.create(dao.findPageBy(operatorCode, vo));
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteSysMessageAction(operatorCode, code).run();
    }

    @Override
    public void deleteAll(String operatorCode, String type) {
        new DeleteAllSysMessageAction(operatorCode, type).run();
    }

    @Override
    public SysMessageCountInfo findUnreadCountBy(String operatorCode) {
        SysMessageCountInfo countInfo = messageMapper.selectUnreadCount(operatorCode);
        countInfo.setNotice(noticeDao.findUnreadCount(operatorCode));
        return countInfo;
    }

    @Override
    public SysMessageInfo findByCode(String operatorCode, String code) {
        return assembler.create(dao.findByCode(operatorCode, code));
    }
}
