package com.lego.system.controller;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.service.ISysOperationLogService;
import com.lego.system.vo.SysOperationLogSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-operation-log")
public class SysOperationLogController extends BaseController {

    @Autowired
    private ISysOperationLogService operationLogService;

    @PostMapping("/list")
    public JsonResponse<LegoPage<SysOperationLogInfo>> list(@RequestBody SysOperationLogSearchVO vo) {
        return JsonResponse.success(operationLogService.findBy(vo));
    }

    @PostMapping("/list-entity")
    public JsonResponse<List<SysOperationLogInfo>> listEntity(String entityCode, String permissionCode) {
        return JsonResponse.success(operationLogService.findBy(getLoginCode(), entityCode, permissionCode));
    }

}
