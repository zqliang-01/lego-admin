package com.lego.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.core.dto.TypeInfo;
import com.lego.report.dto.ReportDefinitionInfo;
import com.lego.report.vo.ReportConditionVO;
import com.lego.report.vo.ReportDefinitionCreateVO;
import com.lego.report.vo.ReportDefinitionModifyVO;

import java.util.List;

public interface IReportDefinitionService {

    List<TypeInfo> findSimpleType(String code, String name, Boolean enable);

    ReportDefinitionInfo findBy(String code);

    void update(String operatorCode, ReportDefinitionModifyVO vo);

    TypeInfo add(String operatorCode, ReportDefinitionCreateVO vo);

    void delete(String operatorCode, List<String> codes);

    <M> IPage<M> openTestSql(String sqlText, List<ReportConditionVO> vos);

}
