package com.lego.report.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.common.Constants;
import com.lego.core.data.ICommonService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.report.dto.ReportDefinitionInfo;
import com.lego.report.service.IReportDefinitionService;
import com.lego.report.vo.ReportDefinitionCreateVO;
import com.lego.report.vo.ReportDefinitionModifyVO;
import com.lego.report.vo.ReportOpenTestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/report-definition")
public class ReportDefinitionController extends BaseController {

    @Autowired
    private IReportDefinitionService definitionService;

    @Autowired
    private ICommonService commonService;

    @PostMapping("/add")
    @SaCheckPermission("report_definition_add")
    public JsonResponse<TypeInfo> add(@RequestBody ReportDefinitionCreateVO vo) {
        return JsonResponse.success(definitionService.add(getLoginCode(), vo));
    }

    @PostMapping("/update")
    @SaCheckPermission("report_definition_update")
    public JsonResponse<Object> update(@RequestBody ReportDefinitionModifyVO vo) {
        definitionService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("report_definition_delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        definitionService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @GetMapping("/list-simple")
    @SaCheckPermission("report_definition_read")
    public JsonResponse<List<TypeInfo>> list(String code, String name) {
        return JsonResponse.success(definitionService.findSimpleType(code, name, null));
    }

    @GetMapping("/list-simple-valid")
    public JsonResponse<List<TypeInfo>> listValid(String code, String name) {
        return JsonResponse.success(definitionService.findSimpleType(code, name, true));
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("report_definition_add")
    public JsonResponse<ReportDefinitionInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(definitionService.findBy(code));
    }

    @PostMapping(value = "/open-test")
    @SaCheckPermission("report_definition_update")
    public JsonResponse<Object> openTest(@RequestBody ReportOpenTestVO vo) throws Exception {
        return JsonResponse.success(definitionService.openTestSql(vo.getDataSource(), vo.getSqlText(), vo.getParams()));
    }

    @GetMapping(value = "/permission-get")
    public JsonResponse<ReportDefinitionInfo> getByPermission(String permissionCode) throws Exception {
        if (!StpUtil.getRoleList().contains(Constants.ADMIN_ROLE_CODE)) {
            StpUtil.checkPermission(permissionCode);
        }
        String code = commonService.findRelateCodeBy(permissionCode);
        ReportDefinitionInfo definitionInfo = definitionService.findBy(code);
        BusinessException.check(definitionInfo.isEnable(), "报表[{0}]已停用，暂时无法提供查询操作！", definitionInfo.getName());
        return JsonResponse.success(definitionInfo);
    }

}
