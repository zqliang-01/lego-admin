package com.lego.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysCodeGeneratorInfo;
import com.lego.system.service.ISysCodeGeneratorService;
import com.lego.system.vo.SysCodeGeneratorCreateVO;
import com.lego.system.vo.SysCodeGeneratorModifyVO;
import com.lego.system.vo.SysPermissionCode;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sys-code-generator")
public class SysCodeGeneratorController extends BaseController {

	@Autowired
	private ISysCodeGeneratorService generatorService;

    @PostMapping("/add")
    @SaCheckPermission(SysPermissionCode.manage)
    public JsonResponse<TypeInfo> add(@RequestBody SysCodeGeneratorCreateVO vo) {
    	TypeInfo info = generatorService.add(getLoginCode(), vo);
        return JsonResponse.success(info);
    }

    @PostMapping("/update")
    @SaCheckPermission(SysPermissionCode.manage)
    public JsonResponse<TypeInfo> update(@RequestBody SysCodeGeneratorModifyVO vo) {
    	TypeInfo info = generatorService.update(getLoginCode(), vo);
        return JsonResponse.success(info);
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission(SysPermissionCode.manage)
    public JsonResponse<Object> delete(@PathVariable String code) {
    	generatorService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

    @GetMapping("/get/{code}")
    public JsonResponse<SysCodeGeneratorInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(generatorService.findByCode(code));
    }

    @GetMapping("/generate/{code}")
    public JsonResponse<String> generate(@PathVariable String code) {
        return JsonResponse.success(generatorService.generate(code));
    }
}
