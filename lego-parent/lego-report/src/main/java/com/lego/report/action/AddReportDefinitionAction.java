package com.lego.report.action;

import com.lego.core.action.AddAction;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.vo.ReportDefinitionCreateVO;

public class AddReportDefinitionAction extends AddAction<ReportDefinition, IReportDefinitionDao> {

    private ReportDefinitionCreateVO vo;

    public AddReportDefinitionAction(String operatorCode, ReportDefinitionCreateVO vo) {
        super("report_definition", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "姓名不能为空，报表定义表新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getDataSource()), "数据源不能为空，报表定义表新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getSqlText()), "SQL脚本不能为空，报表定义表新增失败！");
    }

    @Override
    protected ReportDefinition createTargetEntity() {
        ReportDefinition entity = new ReportDefinition(vo.getName());
        entity.setDataSource(vo.getDataSource());
        entity.setSn(vo.getSn());
        entity.setEnable(vo.isEnable());
        entity.setSqlText(vo.getSqlText());
        entity.setMaxExportSize(vo.getMaxExportSize());
        entity.setCreatorCode(operatorCode);
        return entity;
    }

    @Override
    protected void postprocess() {
        new AddReportTitleAction(operatorCode, targetEntity, vo.getTitles()).run();
        new AddReportConditionAction(operatorCode, targetEntity, vo.getParams()).run();
    }

    public TypeInfo getTypeInfo() {
        return EntityUtil.toTypeInfo(targetEntity);
    }
}
