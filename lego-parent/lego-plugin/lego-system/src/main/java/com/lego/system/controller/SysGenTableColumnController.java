package com.lego.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.service.ISysGenTableColumnService;
import com.lego.system.vo.SysGenTableColumnModifyVO;

@RestController
@RequestMapping("/back-end/sys-gen-table-column")
public class SysGenTableColumnController extends BaseController {

	@Autowired
	private ISysGenTableColumnService columnService;

    @GetMapping("/list")
    public JsonResponse<List<SysGenTableColumnInfo>> list(String tableCode) {
        return JsonResponse.success(columnService.findByTable(tableCode));
    }

    @PostMapping("/modify")
    public JsonResponse<Object> modify(@RequestBody List<SysGenTableColumnModifyVO> vos) {
    	columnService.modify(getLoginCode(), vos);
        return JsonResponse.success();
    }

}
