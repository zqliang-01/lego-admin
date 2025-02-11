package com.lego.system.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysPrintLogInfo;
import com.lego.system.service.ISysPrintLogService;
import com.lego.system.vo.SysPrintLogCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-print-log")
public class SysPrintLogController extends BaseController {

    @Autowired
    private ISysPrintLogService logService;

    @GetMapping("/list")
    public JsonResponse<List<SysPrintLogInfo>> list(String permissionCode, String entityCode) {
        return JsonResponse.success(logService.findBy(permissionCode, entityCode));
    }

    @GetMapping("/get/{code}")
    public JsonResponse<SysPrintLogInfo> get(@PathVariable String code) {
        return JsonResponse.success(logService.findBy(code));
    }

    @PostMapping("/add")
    public JsonResponse<Object> add(@RequestBody SysPrintLogCreateVO vo) {
        logService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
