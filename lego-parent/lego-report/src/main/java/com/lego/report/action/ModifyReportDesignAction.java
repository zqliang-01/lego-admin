package com.lego.report.action;

import cn.hutool.core.collection.CollUtil;
import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.dao.IReportDesignDao;
import com.lego.report.entity.ReportDesign;
import com.lego.report.vo.ReportDesignModifyVO;

public class ModifyReportDesignAction extends ModifyAction<ReportDesign, IReportDesignDao> {

    private ReportDesignModifyVO vo;
    private IReportDefinitionDao definitionDao = getDao(IReportDefinitionDao.class);

    public ModifyReportDesignAction(String operatorCode, ReportDesignModifyVO vo) {
        super("report_design", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，修改失败！");
        if (!"top".equals(vo.getPosition())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getChartType()), "图形类型不能为空，修改失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getDefinition()), "报表定义不能为空，修改失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getPosition()), "位置不能为空，修改失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getDataDimension()), "数据维度字段不能为空，修改失败！");
            BusinessException.check(CollUtil.isNotEmpty(vo.getDataCategories()), "数据分类字段不能为空，修改失败！");
        }
    }

    @Override
    protected void doModify(ReportDesign design) {
        design.setSn(vo.getSn());
        design.setName(vo.getName());
        design.setCreatorCode(operatorCode);
        design.setDefinition(definitionDao.findByCode(vo.getDefinition()));
        design.setDataCategories(vo.getDataCategories());
        design.setEnable(vo.isEnable());
        design.setDataDimension(vo.getDataDimension());
        design.setChartType(vo.getChartType());
    }
}
