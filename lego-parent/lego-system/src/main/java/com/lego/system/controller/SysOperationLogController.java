package com.lego.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.service.ISysOperationLogService;

@RestController
@RequestMapping("/back-end/sys-operation-log")
public class SysOperationLogController extends BaseController {

	@Autowired
	private ISysOperationLogService operationLogService;

    @PostMapping("/list")
    public JsonResponse<List<SysOperationLogInfo>> list(String entityCode, String permissionCode) {
        return JsonResponse.success(operationLogService.findBy(getLoginCode(), entityCode, permissionCode));
    }

}
