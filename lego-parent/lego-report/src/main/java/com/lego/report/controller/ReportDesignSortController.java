package com.lego.report.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.report.service.IReportDesignSortService;
import com.lego.report.vo.ReportDesignSortVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/report-design-sort")
public class ReportDesignSortController extends BaseController {

    @Autowired
    private IReportDesignSortService sortService;

    @PostMapping("/change")
    public JsonResponse<Object> changeSort(@RequestBody ReportDesignSortVO vo) {
        sortService.changeSort(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
