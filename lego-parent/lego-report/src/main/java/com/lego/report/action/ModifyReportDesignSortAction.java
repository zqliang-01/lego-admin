package com.lego.report.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.report.dao.IReportDesignDao;
import com.lego.report.entity.ReportDesign;
import com.lego.report.vo.ReportDesignSortModifyVO;

import java.util.List;
import java.util.Optional;

public class ModifyReportDesignSortAction extends MaintainAction {

    private ReportDesignSortModifyVO vo;

    private IReportDesignDao designDao = getDao(IReportDesignDao.class);

    public ModifyReportDesignSortAction(String operatorCode, ReportDesignSortModifyVO vo) {
        super("report_design", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void doRun() {
        List<ReportDesign> lefts = designDao.findByCodes(vo.getLeftSort());
        for (int i = 0; i < vo.getLeftSort().size(); i++) {
            String code = vo.getLeftSort().get(i);
            Optional<ReportDesign> design = lefts.stream()
                .filter(left -> left.getCode().equals(code))
                .findFirst();
            if (design.isPresent()) {
                design.get().setSn(i + 1);
            }
        }
        designDao.saveAll(lefts);
        List<ReportDesign> rights = designDao.findByCodes(vo.getRightSort());
        for (int i = 0; i < vo.getRightSort().size(); i++) {
            String code = vo.getRightSort().get(i);
            Optional<ReportDesign> design = rights.stream()
                .filter(right -> right.getCode().equals(code))
                .findFirst();
            if (design.isPresent()) {
                design.get().setSn(i + 1);
            }
        }
        designDao.saveAll(rights);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "更新首页排序";
    }
}
