package com.lego.report.action;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.data.mybatis.MybatisDynamicExecutor;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.ExcelUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.report.assembler.ReportConditionAssembler;
import com.lego.report.dao.IReportConditionDao;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.dao.IReportTitleDao;
import com.lego.report.entity.ReportCondition;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.entity.ReportTitle;
import com.lego.report.vo.ReportExportVO;
import org.mybatis.spring.SqlSessionTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExportReportAction extends MaintainAction {

    private ReportExportVO vo;
    private ReportDefinition definition;
    private Map<String, Object> params;

    private IReportTitleDao titleDao = getDao(IReportTitleDao.class);
    private IReportConditionDao conditionDao = getDao(IReportConditionDao.class);
    private IReportDefinitionDao definitionDao = getDao(IReportDefinitionDao.class);
    private MybatisDynamicExecutor executor = LegoBeanFactory.getBean(MybatisDynamicExecutor.class);
    private SqlSessionTemplate sqlSessionTemplate = LegoBeanFactory.getBean(SqlSessionTemplate.class);
    private ReportConditionAssembler conditionAssembler = LegoBeanFactory.getBean(ReportConditionAssembler.class);

    public ExportReportAction(ReportExportVO exportVO) {
        super(exportVO.getPermissionCode(), exportVO.getOperatorCode());
        this.vo = exportVO;
    }

    @Override
    protected void preprocess() {
        definition = definitionDao.findByCode(vo.getCode());
        BusinessException.check(definition.isEnable(), "报表[{0}]已停用，暂时无法提供导出操作！", definition.getName());

        List<ReportCondition> conditions = conditionDao.findBy(definition);
        params = conditionAssembler.convertParams(conditions, vo.getParam());
        long count = executor.selectCount(sqlSessionTemplate, definition.getSqlText(), params);
        BusinessException.check(count <= definition.getMaxExportSize(), "报表[{0}]导出结果集超过最大导出数量[{1}]，请缩小导出范围！", definition.getName(), definition.getMaxExportSize());
    }

    @Override
    protected String getEntityName() {
        return definition.getName();
    }

    @Override
    protected void doRun() {
        List<List<String>> head = new ArrayList<>();
        List<ReportTitle> titles = titleDao.findBy(definition);
        DynamicDataSourceContextHolder.push(definition.getDataSource());
        List<Map<String, Object>> results = executor.select(sqlSessionTemplate, definition.getSqlText(), params);

        for (ReportTitle title : titles) {
            head.add(Arrays.asList(title.getName()));
        }
        List<List<String>> data = new ArrayList<>();
        for (Map<String, Object> result : results) {
            List<String> values = new ArrayList<>();
            for (ReportTitle title : titles) {
                values.add(StringUtil.toString(result.get(title.getSqlKey())));
            }
            data.add(values);
        }
        ExcelUtil.exportExcel(head, data, definition.getName(), vo.getResponse());
        this.description = MessageFormat.format("导出报表数据[{1}]条，报表导出条件[{0}]", results.size(), vo.getParam());
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.EXPORT;
    }
}
