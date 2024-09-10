package com.lego.report.action;

import cn.hutool.core.collection.CollUtil;
import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.dao.IReportDesignDao;
import com.lego.report.entity.ReportDesign;
import com.lego.report.vo.ReportDesignCreateVO;

public class AddReportDesignAction extends AddAction<ReportDesign, IReportDesignDao> {

    private ReportDesignCreateVO vo;
    private IReportDefinitionDao definitionDao = getDao(IReportDefinitionDao.class);

    public AddReportDesignAction(String operatorCode, ReportDesignCreateVO vo) {
        super("report_design", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，新增失败！");
        if (!"top".equals(vo.getPosition())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getChartType()), "图形类型不能为空，新增失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getDefinition()), "报表定义不能为空，新增失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getPosition()), "位置不能为空，新增失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getDataDimension()), "数据维度字段不能为空，新增失败！");
            BusinessException.check(CollUtil.isNotEmpty(vo.getDataCategories()), "数据分类字段不能为空，新增失败！");
        }
    }

    @Override
    protected ReportDesign createTargetEntity() {
        ReportDesign design = new ReportDesign(vo.getName());
        design.setSn(vo.getSn());
        design.setCreatorCode(operatorCode);
        design.setDefinition(definitionDao.findByCode(vo.getDefinition()));
        design.setDataCategories(vo.getDataCategories());
        design.setEnable(vo.isEnable());
        design.setPosition(vo.getPosition());
        design.setDataDimension(vo.getDataDimension());
        design.setChartType(vo.getChartType());
        return design;
    }
}
