package com.lego.sharding.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.ExcelUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.sharding.config.ShardingDataSourceConfig;
import com.lego.sharding.dto.ShardingConfigInfo;
import com.lego.sharding.service.IShardingConfigService;
import com.lego.sharding.vo.ShardingConfigCreateVO;
import com.lego.sharding.vo.ShardingConfigModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/back-end/sharding-config")
public class ShardingConfigController extends BaseController {

    @Autowired
    private IShardingConfigService configService;

    @Autowired
    private ShardingDataSourceConfig shardingDataSourceConfig;

    @PostMapping("/add")
    @SaCheckPermission("manage_sharding_config_add")
    public JsonResponse<Object> add(@RequestBody ShardingConfigCreateVO vo) {
        configService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("manage_sharding_config_update")
    public JsonResponse<Object> update(@RequestBody ShardingConfigModifyVO vo) {
        configService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("manage_sharding_config_delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        configService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("manage_sharding_config_read")
    public JsonResponse<LegoPage<ShardingConfigInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(configService.findPageBy(vo));
    }

    @GetMapping("/list-simple")
    @SaCheckPermission("manage_sharding")
    public JsonResponse<List<TypeInfo>> listSimple() {
        return JsonResponse.success(configService.findSimpleType());
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("manage_sharding_config_detail")
    public JsonResponse<ShardingConfigInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(configService.findBy(code));
    }

    @PostMapping("/export")
    @SaCheckPermission("manage_sharding_config_export")
    public void exportAll(@RequestBody List<String> codes, HttpServletResponse response) {
        List<ShardingConfigInfo> datas = configService.findBy(codes);
        ExcelUtil.exportExcel(datas, "分片规则数据", ShardingConfigInfo.class, response);
    }

    @PostMapping("/export-all")
    @SaCheckPermission("manage_sharding_config_export")
    public void exportAll(@RequestBody GenericSearchVO vo, HttpServletResponse response) {
        List<ShardingConfigInfo> datas = configService.findBy(vo);
        ExcelUtil.exportExcel(datas, "分片规则数据", ShardingConfigInfo.class, response);
    }

    @PostMapping("/test/{id}")
    @SaCheckPermission("manage_sharding_config_update")
    public JsonResponse<List> test(@PathVariable Long id, String sql) throws Exception {
        BusinessException.check(StringUtil.isNotBlank(sql), "执行SQL不能为空！");
        return JsonResponse.success(shardingDataSourceConfig.test(id, sql));
    }

}
