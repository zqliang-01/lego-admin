package com.lego.system.controller.remote;

import com.lego.core.common.ServiceStartType;
import com.lego.core.data.ICommonService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.feign.api.ISystemAPI;
import com.lego.core.vo.ActionVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.vo.SysMessageCreateVO;
import com.lego.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-common")
@ConditionalOnProperty(name = "lego.start-type", havingValue = ServiceStartType.microservice)
public class SysCommonController extends BaseController implements ISystemAPI {

    @Autowired
    private ICommonService commonService;

    @PostMapping("/add-log")
    public JsonResponse<Object> addLog(@RequestBody ActionVO vo) {
        commonService.addLog(vo);
        return JsonResponse.success();
    }

    @Override
    @GetMapping("/get-employee-simple/{code}")
    public JsonResponse<TypeInfo> findEmployeeBy(String code) {
        return JsonResponse.success(commonService.findEmployeeBy(code));
    }

    @Override
    @GetMapping("/get-dept-simple/{code}")
    public JsonResponse<TypeInfo> findDeptBy(String code) {
        return JsonResponse.success(commonService.findDeptBy(code));
    }

    @Override
    @PostMapping("/add-message")
    public JsonResponse<Object> addSysMessage(SysMessageCreateVO vo) {
        commonService.addSysMessage(vo);
        return JsonResponse.success();
    }

    @Override
    @GetMapping("/get-relate-code/{permissionCode}")
    public JsonResponse<String> findRelateCodeBy(String permissionCode) {
        return JsonResponse.success(commonService.findRelateCodeBy(permissionCode));
    }

    @Override
    @GetMapping("/list-permission-code")
    public JsonResponse<List<String>> findPermissionCodesBy(String employeeCode) {
        return JsonResponse.success(commonService.findPermissionCodesBy(employeeCode));
    }

    @Override
    @GetMapping("/list-role-code")
    public JsonResponse<List<String>> findRoleCodesBy(String employeeCode) {
        return JsonResponse.success(commonService.findRoleCodesBy(employeeCode));
    }

    @Override
    @GetMapping("/list-data-permission-employee-code")
    public JsonResponse<List<String>> findDataPermissionEmployeeCode() {
        return JsonResponse.success(commonService.findDataPermissionEmployeeCode());
    }

    @Override
    public JsonResponse<String> findPermissionCodeByTable(String tableCode) {
        return JsonResponse.success(commonService.findPermissionCodeByTable(tableCode));
    }

}
