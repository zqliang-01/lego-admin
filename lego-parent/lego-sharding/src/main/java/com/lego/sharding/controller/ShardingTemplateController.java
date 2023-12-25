package com.lego.sharding.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.ExcelUtil;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.sharding.dto.ShardingTemplateInfo;
import com.lego.sharding.service.IShardingTemplateService;
import com.lego.sharding.vo.ShardingTemplateCreateVO;
import com.lego.sharding.vo.ShardingTemplateModifyVO;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sharding-template")
public class ShardingTemplateController extends BaseController {

    @Autowired
    private IShardingTemplateService templateService;

    @PostMapping("/add")
    @SaCheckPermission("manage:sharding:template:add")
    public JsonResponse<Object> add(@RequestBody ShardingTemplateCreateVO vo) {
        templateService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("manage:sharding:template:update")
    public JsonResponse<Object> update(@RequestBody ShardingTemplateModifyVO vo) {
        templateService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("manage:sharding:template:delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        templateService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("manage:sharding:template:read")
    public JsonResponse<LegoPage<ShardingTemplateInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(templateService.findPageBy(vo));
    }

    @GetMapping("/list-simple")
    @SaCheckPermission("manage:sharding")
    public JsonResponse<List<TypeInfo>> listSimple(String typeCode) {
        return JsonResponse.success(templateService.findSimpleTypeBy(typeCode));
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("manage:sharding:template:detail")
    public JsonResponse<ShardingTemplateInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(templateService.findBy(code));
    }

    @GetMapping("/getJson/{code}")
    @SaCheckPermission("manage:sharding")
    public JsonResponse<String> getJsonByCode(@PathVariable String code) {
        return JsonResponse.success(templateService.findJsonBy(code));
    }

    @PostMapping("/export")
    @SaCheckPermission("manage:sharding:template:export")
    public void exportAll(@RequestBody List<String> codes, HttpServletResponse response) {
        List<ShardingTemplateInfo> datas = templateService.findBy(codes);
        ExcelUtil.exportExcel(datas, "分片模板数据", ShardingTemplateInfo.class, response);
    }

    @PostMapping("/export-all")
    @SaCheckPermission("manage:sharding:template:export")
    public void exportAll(@RequestBody GenericSearchVO vo, HttpServletResponse response) {
        List<ShardingTemplateInfo> datas = templateService.findBy(vo);
        ExcelUtil.exportExcel(datas, "分片模板数据", ShardingTemplateInfo.class, response);
    }

}
