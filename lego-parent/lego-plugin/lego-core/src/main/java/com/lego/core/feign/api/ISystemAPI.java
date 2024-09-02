package com.lego.core.feign.api;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.ActionVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.vo.SysMessageCreateVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ISystemAPI {

    /**
     * 写操作日志，会存在量大的问题，正式使用建议写ES并改造日志查询逻辑
     */
    @PostMapping("/back-end/sys-common/add-log")
    JsonResponse<Object> addLog(@RequestBody ActionVO vo);

    @GetMapping("/back-end/sys-common/get-employee-simple/{code}")
    JsonResponse<TypeInfo> findEmployeeBy(@PathVariable String code);

    @GetMapping("/back-end/sys-common/get-dept-simple/{code}")
    JsonResponse<TypeInfo> findDeptBy(@PathVariable String code);

    @PostMapping("/back-end/sys-common/add-message")
    JsonResponse<Object> addSysMessage(@RequestBody SysMessageCreateVO vo);

    @GetMapping("/back-end/sys-common/get-relate-code/{permissionCode}")
    JsonResponse<String> findRelateCodeBy(@PathVariable String permissionCode);

    @GetMapping("/back-end/sys-common/list-permission-code")
    JsonResponse<List<String>> findPermissionCodesBy(@RequestParam("employeeCode") String employeeCode);

    @GetMapping("/back-end/sys-common/list-role-code")
    JsonResponse<List<String>> findRoleCodesBy(@RequestParam("employeeCode") String employeeCode);

    @GetMapping("/back-end/sys-common/list-data-permission-employee-code")
    JsonResponse<List<String>> findDataPermissionEmployeeCode();

    @GetMapping(value = "/back-end/sys-common/get-permission-code")
    JsonResponse<String> findPermissionCodeByTable(@RequestParam("tableCode") String tableCode);
}
