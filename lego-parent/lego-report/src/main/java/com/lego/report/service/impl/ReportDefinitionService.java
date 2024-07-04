package com.lego.report.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.data.mybatis.MybatisDynamicExecutor;
import com.lego.core.dto.TypeInfo;
import com.lego.report.action.AddReportDefinitionAction;
import com.lego.report.action.DeleteReportDefinitionAction;
import com.lego.report.action.ModifyReportDefinitionAction;
import com.lego.report.assembler.ReportConditionAssembler;
import com.lego.report.assembler.ReportDefinitionAssembler;
import com.lego.report.assembler.ReportTitleAssembler;
import com.lego.report.dao.IReportConditionDao;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.dao.IReportTitleDao;
import com.lego.report.dto.ReportDefinitionInfo;
import com.lego.report.entity.ReportCondition;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.entity.ReportTitle;
import com.lego.report.service.IReportDefinitionService;
import com.lego.report.vo.ReportConditionVO;
import com.lego.report.vo.ReportDefinitionCreateVO;
import com.lego.report.vo.ReportDefinitionModifyVO;
import com.lego.sharding.config.ShardingHintConfig;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportDefinitionService extends BusService<IReportDefinitionDao, ReportDefinitionAssembler> implements IReportDefinitionService {

    @Autowired
    private IReportConditionDao conditionDao;
    @Autowired
    private ReportConditionAssembler conditionAssembler;
    @Autowired
    private IReportTitleDao titleDao;
    @Autowired
    private ReportTitleAssembler titleAssembler;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private MybatisDynamicExecutor executor;

    @Override
    public List<TypeInfo> findSimpleType(String code, String name, Boolean enable) {
        List<ReportDefinition> definitions = dao.findBy(code, name, enable);
        return assembler.createTypeInfo(definitions);
    }

    @Override
    public ReportDefinitionInfo findBy(String code) {
        ReportDefinition definition = dao.findByCode(code);
        ReportDefinitionInfo definitionInfo = assembler.create(definition);

        List<ReportCondition> conditions = conditionDao.findBy(definition);
        definitionInfo.setParams(conditionAssembler.create(conditions));

        List<ReportTitle> titles = titleDao.findBy(definition);
        definitionInfo.setTitles(titleAssembler.create(titles));
        return definitionInfo;
    }

    @Override
    public void update(String operatorCode, ReportDefinitionModifyVO vo) {
        openTestSql(vo.getDataSource(), vo.getSqlText(), vo.getParams());
        new ModifyReportDefinitionAction(operatorCode, vo).run();
    }

    @Override
    public TypeInfo add(String operatorCode, ReportDefinitionCreateVO vo) {
        openTestSql(vo.getDataSource(), vo.getSqlText(), vo.getParams());
        AddReportDefinitionAction addAction = new AddReportDefinitionAction(operatorCode, vo);
        addAction.run();
        return addAction.getTypeInfo();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteReportDefinitionAction(operatorCode, code).run();
        }
    }

    @Override
    public <M> IPage<M> openTestSql(String dataSource, String sqlText, List<ReportConditionVO> vos) {
        ShardingHintConfig.setDataSource(dataSource);
        Map<String, Object> params = conditionAssembler.convertParams(vos);
        return executor.selectPage(sqlSessionTemplate, sqlText, params, 1, 1);
    }

}
