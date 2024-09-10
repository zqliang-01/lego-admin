package com.lego.report.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.data.mybatis.MybatisDynamicExecutor;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.report.action.ExportReportAction;
import com.lego.report.assembler.ReportConditionAssembler;
import com.lego.report.assembler.ReportDefinitionAssembler;
import com.lego.report.dao.IReportConditionDao;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.dao.IReportTitleDao;
import com.lego.report.dto.ReportDesignOpenInfo;
import com.lego.report.entity.ReportCondition;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.entity.ReportTitle;
import com.lego.report.service.IReportOpenService;
import com.lego.report.vo.ReportDefinitionTypeEnum;
import com.lego.report.vo.ReportExportVO;
import com.lego.report.vo.ReportOpenDashBoardVO;
import com.lego.report.vo.ReportOpenPageVO;
import com.lego.sharding.config.ShardingHintConfig;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportOpenService extends BusService<IReportDefinitionDao, ReportDefinitionAssembler> implements IReportOpenService {

    @Autowired
    private IReportConditionDao conditionDao;
    @Autowired
    private ReportConditionAssembler conditionAssembler;
    @Autowired
    private IReportTitleDao titleDao;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private MybatisDynamicExecutor executor;

    @Override
    public <M> IPage<M> openPageSql(String code, ReportOpenPageVO vo) {
        ReportDefinition definition = dao.findByCode(code);
        BusinessException.check(definition.isEnable(), "报表[{0}]已停用，暂时无法提供查询操作！", definition.getName());
        List<ReportCondition> conditions = conditionDao.findBy(definition);
        Map<String, Object> params = conditionAssembler.convertParams(conditions, vo.getParam());
        ShardingHintConfig.setDataSource(definition.getDataSource());
        return executor.selectPage(sqlSessionTemplate, definition.getSqlText(), params, vo.getPageSize(), vo.getPageIndex());
    }

    @Override
    public <M> List<M> openConditionList(String code, String conditionCode) {
        ReportCondition condition = conditionDao.findByCode(conditionCode);
        ReportDefinition definition = condition.getDefinition();
        BusinessException.check(code.equals(EntityUtil.getCode(definition)), "报表条件[{0}]定义不匹配，获取报表条件结果失败！", condition.getName());

        ReportDefinition dataDefinition = condition.getDataDefinition();
        BusinessException.check(dataDefinition != null, "报表条件[{0}]未定义数据，获取报表条件结果失败！", condition.getName());
        ShardingHintConfig.setDataSource(definition.getDataSource());
        long count = executor.selectCount(sqlSessionTemplate, dataDefinition.getSqlText(), new HashMap<>());
        BusinessException.check(count <= 100, "报表条件[{0}]结果集超过100，请缩小查询范围！", dataDefinition.getName());
        return executor.select(sqlSessionTemplate, dataDefinition.getSqlText(), new HashMap<>());
    }

    @Override
    public <M> ReportDesignOpenInfo<M> openDashBoardSql(ReportOpenDashBoardVO vo) {
        ReportDefinition definition = dao.findByCode(vo.getCode());
        boolean isDashBroad = ReportDefinitionTypeEnum.isDashBroad(definition.getType());
        BusinessException.check(isDashBroad, "报表[{0}]不是首页大屏报表，数据查询失败！", definition.getName());

        List<ReportCondition> conditions = conditionDao.findBy(definition);
        Map<String, Object> params = conditionAssembler.convertParams(conditions, vo.getParam());
        ShardingHintConfig.setDataSource(definition.getDataSource());
        List<ReportTitle> titles = titleDao.findBy(definition);
        List<Object> results = executor.select(sqlSessionTemplate, definition.getSqlText(), params);
        return new ReportDesignOpenInfo(results, titles);
    }

    @Override
    public void export(ReportExportVO exportVO) {
        new ExportReportAction(exportVO).run();
    }

}
