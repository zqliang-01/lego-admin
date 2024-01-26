package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysCodeGeneratorInfo;
import com.lego.system.entity.SysCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class SysCodeGeneratorAssembler extends EntityAssembler<SysCodeGeneratorInfo, SysCodeGenerator> {

  @Override
  protected SysCodeGeneratorInfo doCreate(SysCodeGenerator entity) {
    SysCodeGeneratorInfo info = new SysCodeGeneratorInfo();
    info.setCode(entity.getCode());
    info.setName(entity.getName());
    info.setPrefix(entity.getPrefix());
    info.setSerialLength(entity.getSerialLength());
    info.setDatePattern(entity.getDatePattern());
    return info;
  }

}
