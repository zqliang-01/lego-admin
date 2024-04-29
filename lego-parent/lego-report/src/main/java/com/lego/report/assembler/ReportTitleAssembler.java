package com.lego.report.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.report.dto.ReportTitleInfo;
import com.lego.report.entity.ReportTitle;
import org.springframework.stereotype.Component;

@Component
public class ReportTitleAssembler extends EntityAssembler<ReportTitleInfo, ReportTitle> {

    @Override
    protected ReportTitleInfo doCreate(ReportTitle entity) {
        ReportTitleInfo info = new ReportTitleInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setSqlKey(entity.getSqlKey());
        info.setSn(entity.getSn());
        info.setAlign(entity.getAlign());
        info.setDefinition(createTypeInfo(entity.getDefinition()));
        return info;
    }
}
