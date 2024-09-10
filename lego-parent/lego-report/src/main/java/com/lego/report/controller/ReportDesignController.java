package com.lego.report.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.report.dto.ReportDesignInfo;
import com.lego.report.service.IReportDesignService;
import com.lego.report.vo.ReportDesignCreateVO;
import com.lego.report.vo.ReportDesignModifyVO;
import com.lego.report.vo.ReportDesignSortModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/report-design")
public class ReportDesignController extends BaseController {

    @Autowired
    private IReportDesignService designService;

    @GetMapping("/list")
    @SaCheckPermission("report_design_read")
    public JsonResponse<List<ReportDesignInfo>> list() {
        return JsonResponse.success(designService.findAll());
    }

    @GetMapping("/list-valid")
    public JsonResponse<List<ReportDesignInfo>> listValid() {
        return JsonResponse.success(designService.findValid(getLoginCode()));
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("report_design_read")
    public JsonResponse<ReportDesignInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(designService.findByCode(code));
    }

    @PostMapping("/add")
    @SaCheckPermission("report_design_add")
    public JsonResponse<Object> add(@RequestBody ReportDesignCreateVO vo) {
        designService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("report_design_update")
    public JsonResponse<Object> update(@RequestBody ReportDesignModifyVO vo) {
        designService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update-sort")
    @SaCheckPermission("report_design_update")
    public JsonResponse<Object> updateSort(@RequestBody ReportDesignSortModifyVO vo) {
        designService.updateSort(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("report_design_delete")
    public JsonResponse<Object> delete(String code) {
        designService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

}
