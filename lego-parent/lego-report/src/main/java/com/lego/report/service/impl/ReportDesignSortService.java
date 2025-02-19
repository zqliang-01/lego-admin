package com.lego.report.service.impl;

import com.lego.report.action.ChangeReportDesignSortAction;
import com.lego.report.service.IReportDesignSortService;
import com.lego.report.vo.ReportDesignSortVO;
import org.springframework.stereotype.Service;

@Service
public class ReportDesignSortService implements IReportDesignSortService {

    @Override
    public void changeSort(String loginCode, ReportDesignSortVO vo) {
        new ChangeReportDesignSortAction(loginCode, vo).run();
    }
}
