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
import com.lego.sharding.dto.ShardingAlgorithmInfo;
import com.lego.sharding.service.IShardingAlgorithmService;
import com.lego.sharding.vo.ShardingAlgorithmCreateVO;
import com.lego.sharding.vo.ShardingAlgorithmModifyVO;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sharding-algorithm")
public class ShardingAlgorithmController extends BaseController {

    @Autowired
    private IShardingAlgorithmService algorithmService;

    @PostMapping("/add")
    @SaCheckPermission("manage:sharding:algorithm:add")
    public JsonResponse<Object> add(@RequestBody ShardingAlgorithmCreateVO vo) {
        algorithmService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("manage:sharding:algorithm:update")
    public JsonResponse<Object> update(@RequestBody ShardingAlgorithmModifyVO vo) {
        algorithmService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("manage:sharding:algorithm:delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        algorithmService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("manage:sharding:algorithm:read")
    public JsonResponse<LegoPage<ShardingAlgorithmInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(algorithmService.findPageBy(vo));
    }

    @GetMapping("/list-simple")
    @SaCheckPermission("manage:sharding")
    public JsonResponse<List<TypeInfo>> listSimple() {
        return JsonResponse.success(algorithmService.findSimpleType());
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("manage:sharding:algorithm:detail")
    public JsonResponse<ShardingAlgorithmInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(algorithmService.findBy(code));
    }

    @PostMapping("/export")
    @SaCheckPermission("manage:sharding:algorithm:export")
    public void exportAll(@RequestBody List<String> codes, HttpServletResponse response) {
        List<ShardingAlgorithmInfo> datas = algorithmService.findBy(codes);
        ExcelUtil.exportExcel(datas, "分片算法数据", ShardingAlgorithmInfo.class, response);
    }

    @PostMapping("/export-all")
    @SaCheckPermission("manage:sharding:algorithm:export")
    public void exportAll(@RequestBody GenericSearchVO vo, HttpServletResponse response) {
        List<ShardingAlgorithmInfo> datas = algorithmService.findBy(vo);
        ExcelUtil.exportExcel(datas, "分片算法数据", ShardingAlgorithmInfo.class, response);
    }

}
