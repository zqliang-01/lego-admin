package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.system.action.AddSysPrintLogAction;
import com.lego.system.assembler.SysPrintLogAssembler;
import com.lego.system.dao.ISysPrintLogDao;
import com.lego.system.dto.SysPrintLogInfo;
import com.lego.system.entity.SysPrintLog;
import com.lego.system.service.ISysPrintLogService;
import com.lego.system.vo.SysPrintLogCreateVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPrintLogService extends BaseService<ISysPrintLogDao, SysPrintLogAssembler> implements ISysPrintLogService {

    @Override
    public LegoPage<SysPrintLogInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<SysPrintLog> logs = dao.findPageBy(buildCondition(vo));
        return assembler.create(logs);
    }

    @Override
    public List<SysPrintLogInfo> findBy(String permissionCode, String entityCode) {
        List<SysPrintLog> logs = dao.findBy(permissionCode, entityCode);
        return assembler.create(logs);
    }

    @Override
    public SysPrintLogInfo findBy(String code) {
        SysPrintLog log = dao.findByCode(code);
        return assembler.create(log);
    }

    @Override
    public void add(String operator, SysPrintLogCreateVO vo) {
        new AddSysPrintLogAction(operator, vo).run();
    }

}
