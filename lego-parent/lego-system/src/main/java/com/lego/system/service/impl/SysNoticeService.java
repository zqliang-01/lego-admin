package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.system.action.DeleteAllSysNoticeAction;
import com.lego.system.action.DeleteSysNoticeAction;
import com.lego.system.action.ReadAllSysNoticeAction;
import com.lego.system.action.ReadSysNoticeAction;
import com.lego.system.assembler.SysNoticeAssembler;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.dto.SysNoticeInfo;
import com.lego.system.service.ISysNoticeService;
import com.lego.system.vo.SysNoticeSearchVO;
import org.springframework.stereotype.Service;

@Service
public class SysNoticeService extends BusService<ISysNoticeDao, SysNoticeAssembler> implements ISysNoticeService {

    @Override
    public void read(String operatorCode, String code) {
        new ReadSysNoticeAction(operatorCode, code).run();
    }

    @Override
    public void readAll(String operatorCode, String type) {
        new ReadAllSysNoticeAction(operatorCode, type).run();
    }

    @Override
    public LegoPage<SysNoticeInfo> findBy(String operatorCode, SysNoticeSearchVO vo) {
        return assembler.create(dao.findPageBy(operatorCode, vo));
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteSysNoticeAction(operatorCode, code).run();
    }

    @Override
    public void deleteAll(String operatorCode, String type) {
        new DeleteAllSysNoticeAction(operatorCode, type).run();
    }

    @Override
    public long findUnreadCountBy(String operatorCode) {
        return dao.findUnreadCount(operatorCode);
    }
}
