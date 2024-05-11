package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysEmployeeInfo;
import com.lego.system.service.ISysEmployeeService;
import com.lego.system.vo.SysEmployeeCreateVO;
import com.lego.system.vo.SysEmployeeCurrentModifyVO;
import com.lego.system.vo.SysEmployeeModifyVO;
import com.lego.system.vo.SysEmployeePasswordModifyVO;
import com.lego.system.vo.SysEmployeeRoleModifyVO;
import com.lego.system.vo.SysEmployeeSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-employee")
public class SysEmployeeController extends BaseController {

    @Autowired
    private ISysEmployeeService employeeService;

    @GetMapping("/list")
    @SaCheckPermission("manage_users_read")
    public JsonResponse<LegoPage<SysEmployeeInfo>> list(SysEmployeeSearchVO vo) {
        LegoPage<SysEmployeeInfo> employees = employeeService.findBy(vo);
        return JsonResponse.success(employees);
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> listSimpleType() {
        return JsonResponse.success(employeeService.findSimpleType());
    }

    @PostMapping("/add")
    @SaCheckPermission("manage_users_add")
    public JsonResponse<Object> add(@RequestBody SysEmployeeCreateVO vo) {
        employeeService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage_users_update")
    public JsonResponse<Object> modify(@RequestBody SysEmployeeModifyVO vo) {
        employeeService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify-current")
    public JsonResponse<Object> modifyCurrent(@RequestBody SysEmployeeCurrentModifyVO vo) {
        employeeService.modifyCurrent(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify-role")
    @SaCheckPermission("manage_users_update")
    public JsonResponse<Object> modifyRole(@RequestBody SysEmployeeRoleModifyVO vo) {
        employeeService.modifyRole(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify-password")
    public JsonResponse<Object> modifyPassword(@RequestBody SysEmployeePasswordModifyVO vo) {
        employeeService.modifyPassword(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @GetMapping("/current")
    public JsonResponse<SysEmployeeInfo> current() {
        return JsonResponse.success(employeeService.findDTOByCode(getLoginCode()));
    }

    @GetMapping("/get/{code}")
    public JsonResponse<SysEmployeeInfo> get(@PathVariable String code) {
        return JsonResponse.success(employeeService.findDTOByCode(code));
    }

    @PostMapping("/reset-password")
    @SaCheckPermission("manage_users_update")
    public JsonResponse<Object> resetPassword(@RequestBody List<String> codes) {
        employeeService.resetPassword(getLoginCode(), codes);
        return JsonResponse.success();
    }
}
