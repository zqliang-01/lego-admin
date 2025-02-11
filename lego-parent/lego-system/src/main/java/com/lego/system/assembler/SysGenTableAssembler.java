package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.entity.SysGenTable;
import org.springframework.stereotype.Component;

@Component
public class SysGenTableAssembler extends EntityAssembler<SysGenTableInfo, SysGenTable> {

    @Override
    protected SysGenTableInfo doCreate(SysGenTable entity) {
        SysGenTableInfo info = new SysGenTableInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setPath(entity.getPath());
        info.setComment(entity.getComment());
        info.setAppCode(entity.getAppCode());
        info.setUrlName(entity.getUrlName());
        info.setClassName(entity.getClassName());
        info.setFieldName(entity.getFieldName());
        info.setPackageName(entity.getPackageName());
        info.setPermissionCode(entity.getPermissionCode());
        info.setCreator(createTypeInfo(entity.getCreator()));
        info.setDataSource(entity.getDataSource());
        return info;
    }

}
