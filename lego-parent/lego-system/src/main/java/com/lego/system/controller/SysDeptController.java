package com.lego.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.TreeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysDeptInfo;
import com.lego.system.service.ISysDeptService;
import com.lego.system.vo.SysDeptCreateVO;
import com.lego.system.vo.SysDeptModifyVO;
import com.lego.system.vo.SysDeptSearchVO;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sys-dept")
public class SysDeptController extends BaseController {

	@Autowired
	private ISysDeptService deptService;

    @GetMapping("/list")
    public JsonResponse<List<SysDeptInfo>> list(SysDeptSearchVO vo) {
    	List<SysDeptInfo> depts = deptService.findBy(vo);
        return JsonResponse.success(depts);
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TreeInfo>> listSimpleType() {
        return JsonResponse.success(deptService.findTreeType());
    }

    @PostMapping("/add")
    @SaCheckPermission("manage:users:add")
    public JsonResponse<Object> add(@RequestBody SysDeptCreateVO vo) {
    	deptService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage:users:update")
    public JsonResponse<Object> modify(@RequestBody SysDeptModifyVO vo) {
    	deptService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage:users:update")
    public JsonResponse<Object> delete(@PathVariable String code) {
    	deptService.deleteBy(code);
        return JsonResponse.success();
    }
}
