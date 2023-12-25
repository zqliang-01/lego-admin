package com.lego.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.service.ISysCustomFormService;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysCustomFormModifyVO;
import com.lego.system.vo.SysCustomFormSearchVO;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sys-custom-form")
public class SysCustomFormController extends BaseController {

	@Autowired
	private ISysCustomFormService customFormService;

    @PostMapping("/list")
    @SaCheckPermission("manage:customForm:read")
    public JsonResponse<LegoPage<SysCustomFormInfo>> list(SysCustomFormSearchVO vo) {
        return JsonResponse.success(customFormService.findBy(vo));
    }

    @PostMapping("/add")
    @SaCheckPermission("manage:customForm:update")
    public JsonResponse<Object> add(@RequestBody SysCustomFormCreateVO vo) {
    	customFormService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage:customForm:update")
    public JsonResponse<Object> modify(@RequestBody SysCustomFormModifyVO vo) {
    	customFormService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage:customForm:update")
    public JsonResponse<Object> delete(@PathVariable String code) {
    	customFormService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

}
