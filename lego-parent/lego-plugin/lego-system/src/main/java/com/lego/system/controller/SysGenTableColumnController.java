package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.MetaTableColumnInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.service.ISysGenTableColumnService;
import com.lego.system.service.ISysGenTableService;
import com.lego.system.vo.SysGenTableColumnCreateVO;
import com.lego.system.vo.SysGenTableColumnModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-gen-table-column")
public class SysGenTableColumnController extends BaseController {

    @Autowired
    private ISysGenTableService tableService;

	@Autowired
	private ISysGenTableColumnService columnService;

    @GetMapping("/list")
    @SaCheckPermission("manage_genTable_read")
    public JsonResponse<List<SysGenTableColumnInfo>> list(String tableCode) {
        return JsonResponse.success(columnService.findByTable(tableCode));
    }

    @GetMapping("/list-meta/{tableCode}")
    @SaCheckPermission("manage_genTable_read")
    public JsonResponse<List<MetaTableColumnInfo>> listMeta(@PathVariable String tableCode) {
        SysGenTableInfo table = tableService.findByCode(tableCode);
        List<MetaTableColumnInfo> tableColumns = columnService.findMetaColumnBy(table.getDataSource(), tableCode);
        return JsonResponse.success(tableColumns);
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage_genTable_design")
    public JsonResponse<Object> modify(@RequestBody SysGenTableColumnModifyVO vo) {
        columnService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/add")
    @SaCheckPermission("manage_genTable_design")
    public JsonResponse<Object> add(@RequestBody SysGenTableColumnCreateVO vo) {
        columnService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage_genTable_design")
    public JsonResponse<Object> delete(@PathVariable String code) {
        columnService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

}
