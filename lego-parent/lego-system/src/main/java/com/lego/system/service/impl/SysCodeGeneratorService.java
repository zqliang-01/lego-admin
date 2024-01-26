package com.lego.system.service.impl;

import com.lego.core.data.IdGenerator;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import com.lego.system.action.AddSysCodeGeneratorAction;
import com.lego.system.action.DeleteSysCodeGeneratorAction;
import com.lego.system.action.ModifySysCodeGeneratorAction;
import com.lego.system.assembler.SysCodeGeneratorAssembler;
import com.lego.system.dao.ISysCodeGeneratorDao;
import com.lego.system.dto.SysCodeGeneratorInfo;
import com.lego.system.entity.SysCodeGenerator;
import com.lego.system.service.ISysCodeGeneratorService;
import com.lego.system.vo.SysCodeGeneratorCreateVO;
import com.lego.system.vo.SysCodeGeneratorModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysCodeGeneratorService extends BusService<ISysCodeGeneratorDao, SysCodeGeneratorAssembler> implements ISysCodeGeneratorService {

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public SysCodeGeneratorInfo findByCode(String code) {
        SysCodeGenerator generator = dao.findByCode(code);
        return assembler.create(generator);
    }

    @Override
    public TypeInfo add(String operatorCode, SysCodeGeneratorCreateVO vo) {
        AddSysCodeGeneratorAction addAction = new AddSysCodeGeneratorAction(operatorCode, vo);
        addAction.run();
        return addAction.getTypeInfo();
    }

    @Override
    public TypeInfo update(String operatorCode, SysCodeGeneratorModifyVO vo) {
        ModifySysCodeGeneratorAction modifyAction = new ModifySysCodeGeneratorAction(operatorCode, vo);
        modifyAction.run();
        return modifyAction.getTypeInfo();
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteSysCodeGeneratorAction(operatorCode, code).run();
    }

    @Override
    public String generate(String code) {
        SysCodeGenerator generator = dao.findByCode(code);
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotBlank(generator.getPrefix())) {
            sb.append(generator.getPrefix());
            sb.append("-");
        }
        if (StringUtil.isNotBlank(generator.getDatePattern())) {
            sb.append(DateUtil.toDateString(DateUtil.currentDateTime(), generator.getDatePattern()));
            sb.append("-");
        }
        long number = idGenerator.nextId("business");
        sb.append(String.format("%" + generator.getSerialLength() + "s", number).replaceAll(" ", "0"));
        return sb.toString();
    }

}
