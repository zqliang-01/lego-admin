package com.lego.system.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.service.ISysAppSortService;
import com.lego.system.service.ISysConfigService;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysConfigCode;
import com.lego.system.vo.SysPermissionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/back-end/sys-app-sort")
public class SysAppSortController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysAppSortService appSortService;

    @GetMapping("/list-all")
    public JsonResponse<List<SysAppInfo>> listAll() {
        List<SysAppInfo> validList = new ArrayList<SysAppInfo>();
        List<SysAppInfo> allApps = permissionService.findAppBy(getLoginCode());
        List<String> sortAppCodes = permissionService.findSortAppBy(getLoginCode());
        List<String> validCodes = configService.findListBy(SysConfigCode.APP_VALID_LIST);
        for (String sortAppCode : sortAppCodes) {
            Optional<SysAppInfo> app = allApps.stream()
                .filter(a -> sortAppCode.equals(a.getCode()) && validCodes.contains(a.getCode()))
                .findFirst();
            validList.add(app.get());
        }
        validList.addAll(allApps.stream()
            .filter(a -> !validList.contains(a)
                && validCodes.contains(a.getCode())
                && !SysPermissionCode.manage.equals(a.getCode()))
            .collect(Collectors.toList()));
        return JsonResponse.success(validList);
    }

    @GetMapping("/list-header")
    public JsonResponse<List<SysAppInfo>> listHeader() {
        List<SysAppInfo> validList = new ArrayList<SysAppInfo>();
        List<String> sortAppCodes = permissionService.findSortAppBy(getLoginCode());
        List<String> validCodes = configService.findListBy(SysConfigCode.APP_VALID_LIST);
        List<SysAppInfo> allApps = permissionService.findAppBy(getLoginCode());
        for (String sortAppCode : sortAppCodes) {
            Optional<SysAppInfo> app = allApps.stream()
                .filter(a -> sortAppCode.equals(a.getCode()) && validCodes.contains(a.getCode()))
                .findFirst();
            validList.add(app.get());
        }
        return JsonResponse.success(validList);
    }

    @PostMapping("/update")
    public JsonResponse<Object> update(@RequestBody List<String> permissionCodes) {
        appSortService.update(getLoginCode(), permissionCodes);
        return JsonResponse.success();
    }
}