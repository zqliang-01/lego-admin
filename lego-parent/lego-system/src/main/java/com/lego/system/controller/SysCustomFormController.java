package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.dto.SysCustomFormPermissionInfo;
import com.lego.system.service.ISysCustomFormService;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysCustomFormModifyVO;
import com.lego.system.vo.SysCustomFormSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-custom-form")
public class SysCustomFormController extends BaseController {

    @Autowired
    private ISysCustomFormService customFormService;

    @PostMapping("/list")
    @SaCheckPermission("manage_customForm_read")
    public JsonResponse<LegoPage<SysCustomFormInfo>> list(@RequestBody SysCustomFormSearchVO vo) {
        return JsonResponse.success(customFormService.findBy(vo));
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> listSimpleType() {
        return JsonResponse.success(customFormService.findSimpleType());
    }

    @GetMapping("/get/{code}")
    public JsonResponse<SysCustomFormInfo> get(@PathVariable String code) {
        return JsonResponse.success(customFormService.findBy(code));
    }

    @GetMapping("/get-permission/{code}")
    public JsonResponse<SysCustomFormPermissionInfo> getPermission(@PathVariable String code) {
        SysCustomFormPermissionInfo permissionInfo = customFormService.findPermissionBy(code);
        return JsonResponse.success(permissionInfo);
    }

    @GetMapping("/get-init")
    @SaCheckPermission("manage_customForm_update")
    public JsonResponse<SysCustomFormInfo> getInit(String tableCode) {
        return JsonResponse.success(customFormService.findInitByTable(tableCode));
    }

    @PostMapping("/add")
    @SaCheckPermission("manage_customForm_update")
    public JsonResponse<Object> add(@RequestBody SysCustomFormCreateVO vo) {
        customFormService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage_customForm_update")
    public JsonResponse<Object> modify(@RequestBody SysCustomFormModifyVO vo) {
        customFormService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage_customForm_update")
    public JsonResponse<Object> delete(@PathVariable String code) {
        customFormService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

}
