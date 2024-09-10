package com.lego.report.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.report.dto.ReportDesignInfo;
import com.lego.report.dto.ReportDesignSimpleInfo;
import com.lego.report.entity.ReportDesign;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportDesignAssembler extends EntityAssembler<ReportDesignInfo, ReportDesign> {

    @Override
    protected ReportDesignInfo doCreate(ReportDesign entity) {
        ReportDesignInfo info = new ReportDesignInfo();
        info.setSn(entity.getSn());
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        info.setPosition(entity.getPosition());
        info.setChartType(entity.getChartType());
        info.setCreatorCode(entity.getCreatorCode());
        info.setDataDimension(entity.getDataDimension());
        info.setDataCategories(entity.getDataCategories());
        info.setDefinition(createTypeInfo(entity.getDefinition()));
        return info;
    }

    public ReportDesignSimpleInfo createSimple(ReportDesign entity) {
        ReportDesignSimpleInfo info = new ReportDesignSimpleInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setDefinitionCode(entity.getDefinition().getCode());
        info.setPosition(entity.getPosition());
        info.setChartType(entity.getChartType());
        info.setSn(entity.getSn());
        info.setEnable(entity.isEnable());
        return info;
    }

    public List<ReportDesignSimpleInfo> createSimple(List<ReportDesign> entities) {
        List<ReportDesignSimpleInfo> infos = new ArrayList<>();
        for (ReportDesign entity : entities) {
            infos.add(createSimple(entity));
        }
        return infos;
    }
}
