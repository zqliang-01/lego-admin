package com.lego.sharding.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.ExcelUtil;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.sharding.dto.ShardingDataSourceInfo;
import com.lego.sharding.service.IShardingDataSourceService;
import com.lego.sharding.vo.ShardingDataSourceCreateVO;
import com.lego.sharding.vo.ShardingDataSourceModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/back-end/sharding-data-source")
public class ShardingDataSourceController extends BaseController {

    @Autowired
    private IShardingDataSourceService dataSourceService;

    @PostMapping("/add")
    @SaCheckPermission("manage_sharding_dataSource_add")
    public JsonResponse<Object> add(@RequestBody ShardingDataSourceCreateVO vo) {
        dataSourceService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("manage_sharding_dataSource_update")
    public JsonResponse<Object> update(@RequestBody ShardingDataSourceModifyVO vo) {
        dataSourceService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("manage_sharding_dataSource_delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        dataSourceService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("manage_sharding_dataSource_read")
    public JsonResponse<LegoPage<ShardingDataSourceInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(dataSourceService.findPageBy(vo));
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> listSimple() {
        return JsonResponse.success(dataSourceService.findSimpleType());
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("manage_sharding_dataSource_detail")
    public JsonResponse<ShardingDataSourceInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(dataSourceService.findBy(code));
    }

    @PostMapping("/export")
    @SaCheckPermission("manage_sharding_dataSource_export")
    public void exportAll(@RequestBody List<String> codes, HttpServletResponse response) {
        List<ShardingDataSourceInfo> datas = dataSourceService.findBy(codes);
        ExcelUtil.exportExcel(datas, "分片数据源数据", ShardingDataSourceInfo.class, response);
    }

    @PostMapping("/export-all")
    @SaCheckPermission("manage_sharding_dataSource_export")
    public void exportAll(@RequestBody GenericSearchVO vo, HttpServletResponse response) {
        List<ShardingDataSourceInfo> datas = dataSourceService.findBy(vo);
        ExcelUtil.exportExcel(datas, "分片数据源数据", ShardingDataSourceInfo.class, response);
    }

}
