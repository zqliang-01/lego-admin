package com.lego.report.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.report.dao.IReportConditionDao;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.entity.ReportCondition;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.vo.ReportConditionVO;

import java.util.ArrayList;
import java.util.List;

public class AddReportConditionAction extends MaintainAction {

    private List<ReportConditionVO> vos;
    private ReportDefinition definition;

    private IReportConditionDao conditionDao = getDao(IReportConditionDao.class);
    private IReportDefinitionDao definitionDao = getDao(IReportDefinitionDao.class);

    protected AddReportConditionAction(String operatorCode, ReportDefinition definition, List<ReportConditionVO> vos) {
        super("report_definition", operatorCode);
        this.vos = vos;
        this.definition = definition;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(definition != null, "报表定义不存在，报表条件创建失败！");
        for (ReportConditionVO vo : vos) {
            BusinessException.check(StringUtil.isNotBlank(vo.getSqlKey()), "报表条件编码不能为空，报表条件创建失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getName()), "报表条件名称不能为空，报表条件创建失败！");
        }

        List<ReportCondition> conditions = conditionDao.findBy(definition);
        conditionDao.deleteAllInBatch(conditions);
    }

    @Override
    protected void doRun() {
        List<ReportCondition> conditions = new ArrayList<>();
        for (ReportConditionVO vo : vos) {
            ReportCondition condition = new ReportCondition(vo.getSqlKey(), vo.getName(), definition);
            condition.setType(vo.getType());
            condition.setRequired(vo.isRequired());
            condition.setDependentCode(vo.getDependentCode());
            if (StringUtil.isNotBlank(vo.getDataDefinitionCode())) {
                condition.setDataDefinition(definitionDao.findByCode(vo.getDataDefinitionCode()));
            }
            condition.setSn(vo.getSn());
            condition.setDefaultValue(vo.getDefaultValue());
            conditions.add(condition);
        }
        conditionDao.saveAll(conditions);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    @Override
    protected String getEntityName() {
        return "批量新增报表条件";
    }

    @Override
    protected void createLog() {
    }
}
