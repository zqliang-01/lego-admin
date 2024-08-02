package com.lego.report.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.common.Constants;
import com.lego.core.data.ICommonService;
import com.lego.core.vo.JsonResponse;
import com.lego.report.service.IReportOpenService;
import com.lego.report.vo.ReportExportVO;
import com.lego.report.vo.ReportOpenPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@SaCheckLogin
@RestController
@RequestMapping("/back-end/report-open")
public class ReportOpenController {

    @Autowired
    private IReportOpenService openService;

    @Autowired
    private ICommonService commonService;

    @PostMapping(value = "/page")
    public JsonResponse openPage(@RequestBody ReportOpenPageVO vo) throws Exception {
        if (!StpUtil.getRoleList().contains(Constants.ADMIN_ROLE_CODE)) {
            StpUtil.checkPermission(vo.getPermissionCode());
        }
        String code = commonService.findReportCodeBy(vo.getPermissionCode());
        return JsonResponse.success(openService.openPageSql(code, vo));
    }

    @GetMapping(value = "/list-condition")
    public JsonResponse listCondition(String permissionCode, String conditionCode) throws Exception {
        if (!StpUtil.getRoleList().contains(Constants.ADMIN_ROLE_CODE)) {
            StpUtil.checkPermission(permissionCode);
        }
        String code = commonService.findReportCodeBy(permissionCode);
        return JsonResponse.success(openService.openConditionList(code, conditionCode));
    }

    @PostMapping(value = "/export")
    public void export(HttpServletResponse response, @RequestBody ReportOpenPageVO vo) throws Exception {
        String permissionCode = vo.getPermissionCode();
        if (!StpUtil.getRoleList().contains(Constants.ADMIN_ROLE_CODE)) {
            StpUtil.checkPermission(permissionCode + "_export");
        }
        ReportExportVO exportVO = new ReportExportVO();
        exportVO.setResponse(response);
        exportVO.setParam(vo.getParam());
        exportVO.setOperatorCode(Constants.loginCode.get());
        exportVO.setPermissionCode(permissionCode);
        exportVO.setCode(commonService.findReportCodeBy(permissionCode));
        openService.export(exportVO);
    }

}
