package com.lego.report.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.report.dto.ReportDefinitionInfo;
import com.lego.report.entity.ReportDefinition;
import org.springframework.stereotype.Component;

@Component
public class ReportDefinitionAssembler extends EntityAssembler<ReportDefinitionInfo, ReportDefinition> {

    @Override
    protected ReportDefinitionInfo doCreate(ReportDefinition entity) {
        ReportDefinitionInfo info = new ReportDefinitionInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setDataSource(entity.getDataSource());
        info.setSn(entity.getSn());
        info.setEnable(entity.isEnable());
        info.setSqlText(entity.getSqlText());
        info.setMaxExportSize(entity.getMaxExportSize());
        info.setCreator(createEmployee(entity.getCreatorCode()));
        return info;
    }
}
