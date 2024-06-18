package com.lego.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.job.service.XxlJobService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/back-end/job-index")
public class IndexController extends BaseController {

    @Resource
    private XxlJobService xxlJobService;

    @GetMapping("/dashboard")
    @SaCheckPermission("job_index_read")
    public JsonResponse<Map<String, Object>> index() {
        return JsonResponse.success(xxlJobService.dashboardInfo());
    }

    @GetMapping("/chartInfo")
    @SaCheckPermission("job_index_read")
    public JsonResponse<Map<String, Object>> chartInfo(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return JsonResponse.success(xxlJobService.chartInfo(startDate, endDate));
    }

}
