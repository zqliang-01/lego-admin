package com.lego.sharding.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.ExcelUtil;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.sharding.dto.ShardingTableInfo;
import com.lego.sharding.service.IShardingTableService;
import com.lego.sharding.vo.ShardingTableCreateVO;
import com.lego.sharding.vo.ShardingTableModifyVO;
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
@RequestMapping("/back-end/sharding-table")
public class ShardingTableController extends BaseController {

    @Autowired
    private IShardingTableService tableService;

    @PostMapping("/add")
    @SaCheckPermission("manage_sharding_table_add")
    public JsonResponse<Object> add(@RequestBody ShardingTableCreateVO vo) {
        tableService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("manage:sharding_table_update")
    public JsonResponse<Object> update(@RequestBody ShardingTableModifyVO vo) {
        tableService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("manage_sharding_table_delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        tableService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("manage_sharding_table_read")
    public JsonResponse<LegoPage<ShardingTableInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(tableService.findPageBy(vo));
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("manage_sharding_table_detail")
    public JsonResponse<ShardingTableInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(tableService.findBy(code));
    }

    @PostMapping("/export")
    @SaCheckPermission("manage_sharding_table_export")
    public void exportAll(@RequestBody List<String> codes, HttpServletResponse response) {
        List<ShardingTableInfo> datas = tableService.findBy(codes);
        ExcelUtil.exportExcel(datas, "分片表数据", ShardingTableInfo.class, response);
    }

    @PostMapping("/export-all")
    @SaCheckPermission("manage_sharding_table_export")
    public void exportAll(@RequestBody GenericSearchVO vo, HttpServletResponse response) {
        List<ShardingTableInfo> datas = tableService.findBy(vo);
        ExcelUtil.exportExcel(datas, "分片表数据", ShardingTableInfo.class, response);
    }

}
