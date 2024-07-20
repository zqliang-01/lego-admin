package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysRoleInfo;
import com.lego.system.entity.SysRole;
import org.springframework.stereotype.Component;

@Component
public class SysRoleAssembler extends EntityAssembler<SysRoleInfo, SysRole> {

    @Override
    protected SysRoleInfo doCreate(SysRole entity) {
        SysRoleInfo info = new SysRoleInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setDataScope(entity.getDataScope());
        info.setCreateTime(entity.getCreateTime());
        return info;
    }
}
