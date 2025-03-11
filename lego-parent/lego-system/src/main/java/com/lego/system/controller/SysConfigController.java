package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.exception.BusinessException;
import com.lego.core.module.version.VersionManager;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysSystemInfo;
import com.lego.system.dto.SysVersionInfo;
import com.lego.system.service.ISysConfigService;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysConfigCode;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/back-end/sys-config")
public class SysConfigController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private VersionManager versionManager;

    @GetMapping("/get-information")
    public JsonResponse<SysSystemInfo> getInformation() {
        return JsonResponse.success(configService.findInformation());
    }

    @PostMapping("/update-information")
    @SaCheckPermission("manage_system_update")
    public JsonResponse<Object> updateInformation(String name, String logo) {
        SysSystemInfo info = configService.findInformation();
        info.setCompanyName(name);
        info.setCompanyLogo(logo);
        configService.update(info.toJson(), SysConfigCode.APP_CONFIG);
        return JsonResponse.success();
    }

    @GetMapping("/check-update")
    @SaCheckPermission("manage_system_update")
    public JsonResponse<SysVersionInfo> checkUpdate() {
        String currentVersion = configService.findValueBy(SysConfigCode.APP_VERSION);
        BusinessException.check(StringUtil.isNotBlank(currentVersion), "当前版本过旧，未检测到版本信息！");
        String newVersion = versionManager.getNewVersion(currentVersion);
        return JsonResponse.success(new SysVersionInfo(currentVersion, newVersion));
    }

    @PostMapping("/update")
    @SaCheckPermission("manage_system_update")
    public JsonResponse<Object> update() {
        String newVersion = versionManager.execUpdate();
        return JsonResponse.success(newVersion);
    }

    @PostMapping("/upload-package")
    @SaCheckPermission("manage_system_update")
    public JsonResponse<Object> uploadPackage(@RequestParam("file") MultipartFile file) {
        configService.importPackage(getLoginCode(), file);
        return JsonResponse.success();
    }

    @GetMapping("/list-app")
    @SaCheckPermission("manage_configSet_read")
    public JsonResponse<Map<String, List<SysAppInfo>>> listApp() {
        List<String> validCodes = configService.findListBy(SysConfigCode.APP_VALID_LIST);
        Map<String, List<SysAppInfo>> results = new HashedMap<String, List<SysAppInfo>>();
        List<SysAppInfo> validList = new ArrayList<SysAppInfo>();
        List<SysAppInfo> inValidList = new ArrayList<SysAppInfo>();
        for (SysAppInfo app : permissionService.findAllApp()) {
            if (validCodes.contains(app.getCode())) {
                validList.add(app);
                continue;
            }
            inValidList.add(app);
        }
        results.put("validList", validList);
        results.put("inValidList", inValidList);
        return JsonResponse.success(results);
    }

    @PostMapping("/set-app")
    @SaCheckPermission("manage_configSet_update")
    public JsonResponse<Object> setApp(String code, boolean enable) {
        List<String> validList = configService.findListBy(SysConfigCode.APP_VALID_LIST);
        if (enable) {
            validList.add(code);
            configService.update(validList, SysConfigCode.APP_VALID_LIST);
            return JsonResponse.success();
        }
        validList.remove(code);
        configService.update(validList, SysConfigCode.APP_VALID_LIST);
        return JsonResponse.success();
    }
}
