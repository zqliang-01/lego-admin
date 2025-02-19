package com.lego.report.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.enums.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.report.dao.IReportTitleDao;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.entity.ReportTitle;
import com.lego.report.vo.ReportTitleVO;

import java.util.ArrayList;
import java.util.List;

public class AddReportTitleAction extends MaintainAction {

    private List<ReportTitleVO> vos;
    private ReportDefinition definition;
    private IReportTitleDao titleDao = getDao(IReportTitleDao.class);

    protected AddReportTitleAction(String operatorCode, ReportDefinition definition, List<ReportTitleVO> vos) {
        super("report_definition", operatorCode);
        this.vos = vos;
        this.definition = definition;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(definition != null, "报表定义不存在，报表表头创建失败！");
        for (ReportTitleVO vo : vos) {
            BusinessException.check(StringUtil.isNotBlank(vo.getSqlKey()), "报表表头编码不能为空，报表表头创建失败！");
            BusinessException.check(StringUtil.isNotBlank(vo.getName()), "报表表头名称不能为空，报表表头创建失败！");
        }

        List<ReportTitle> titles = titleDao.findBy(definition);
        titleDao.deleteAllInBatch(titles);
    }

    @Override
    protected void doRun() {
        List<ReportTitle> titles = new ArrayList<>();
        for (ReportTitleVO vo : vos) {
            ReportTitle title = new ReportTitle(vo.getSqlKey(), vo.getName(), definition);
            title.setSn(vo.getSn());
            title.setAlign(vo.getAlign());
            titles.add(title);
        }
        titleDao.saveAll(titles);
    }

    @Override
    protected String getEntityName() {
        return "批量新增报表表头";
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    @Override
    protected void createLog() {
    }
}
