package com.lego.report.action;

import com.lego.core.action.DeleteAction;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.entity.ReportDefinition;

public class DeleteReportDefinitionAction extends DeleteAction<ReportDefinition, IReportDefinitionDao> {

    public DeleteReportDefinitionAction(String operatorCode, String code) {
        super("report_definition", operatorCode, code);
    }
}
