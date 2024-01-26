package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysFileInfo;
import com.lego.system.entity.SysFile;
import org.springframework.stereotype.Component;

@Component
public class SysFileAssembler extends EntityAssembler<SysFileInfo, SysFile> {

  @Override
  protected SysFileInfo doCreate(SysFile entity) {
    SysFileInfo info = new SysFileInfo();
    info.setCode(entity.getCode());
    info.setCreateTime(entity.getCreateTime());
    info.setCreator(createTypeInfo(entity.getCreator()));
    info.setType(createTypeInfo(entity.getType()));
    info.setName(entity.getName());
    info.setSize(entity.getSize());
    return info;
  }

}
