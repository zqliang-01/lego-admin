package com.lego.report.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.enums.ActionType;
import com.lego.report.dao.IReportDesignSortDao;
import com.lego.report.entity.ReportDesignSort;
import com.lego.report.vo.ReportDesignSortItemVO;
import com.lego.report.vo.ReportDesignSortVO;

import java.util.ArrayList;
import java.util.List;

public class ChangeReportDesignSortAction extends MaintainAction {

    private ReportDesignSortVO vo;
    private IReportDesignSortDao designSortDao = getDao(IReportDesignSortDao.class);

    public ChangeReportDesignSortAction(String operatorCode, ReportDesignSortVO vo) {
        super("report", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void doRun() {
        List<ReportDesignSort> sorts = designSortDao.findBy(operatorCode);
        designSortDao.deleteAll(sorts);

        List<ReportDesignSort> newSorts = new ArrayList<>();
        for (int i = 0; i < vo.getLeftSort().size(); i++) {
            ReportDesignSortItemVO left = vo.getLeftSort().get(i);
            ReportDesignSort sort = new ReportDesignSort(operatorCode, left.getDesignCode());
            sort.setSn(i + 1);
            sort.setEnable(left.isEnable());
            newSorts.add(sort);
        }
        for (int i = 0; i < vo.getRightSort().size(); i++) {
            ReportDesignSortItemVO right = vo.getRightSort().get(i);
            ReportDesignSort sort = new ReportDesignSort(operatorCode, right.getDesignCode());
            sort.setSn(i + 1);
            sort.setEnable(right.isEnable());
            newSorts.add(sort);
        }

        designSortDao.saveAll(newSorts);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "维护首页图表排序";
    }
}
