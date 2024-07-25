package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.entity.SysColumnSort;
import org.springframework.stereotype.Component;

@Component
public class SysColumnSortAssembler extends EntityAssembler<SysColumnSortInfo, SysColumnSort> {

    @Override
    protected SysColumnSortInfo doCreate(SysColumnSort entity) {
        SysColumnSortInfo info = new SysColumnSortInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setVisible(entity.isVisible());
        info.setSn(entity.getSn());
        return info;
    }

}
