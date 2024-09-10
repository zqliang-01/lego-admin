package com.lego.report.action;

import com.lego.core.action.DeleteAction;
import com.lego.report.dao.IReportDesignDao;
import com.lego.report.entity.ReportDesign;

public class DeleteReportDesignAction extends DeleteAction<ReportDesign, IReportDesignDao> {

    public DeleteReportDesignAction(String operatorCode, String entityCode) {
        super("report_design", operatorCode, entityCode);
    }
}
