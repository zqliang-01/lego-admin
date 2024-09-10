package com.lego.report.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.vo.ReportDefinitionModifyVO;

public class ModifyReportDefinitionAction extends ModifyAction<ReportDefinition, IReportDefinitionDao> {

    private ReportDefinitionModifyVO vo;

    public ModifyReportDefinitionAction(String operatorCode, ReportDefinitionModifyVO vo) {
        super("report_definition", operatorCode, vo.getCode());
        this.setCheckDiff(false);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，报表定义表修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "姓名不能为空，报表定义表修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getSqlText()), "SQL脚本不能为空，报表定义表修改失败！");
    }

    @Override
    protected void doModify(ReportDefinition entity) {
        entity.setName(vo.getName());
        entity.setDataSource(vo.getDataSource());
        entity.setSn(vo.getSn());
        entity.setEnable(vo.isEnable());
        entity.setSqlText(vo.getSqlText());
        entity.setMaxExportSize(vo.getMaxExportSize());
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
