package com.lego.system.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysFileInfo;
import com.lego.system.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/back-end/sys-file")
public class SysFileController extends BaseController {

    @Autowired
    private ISysFileService fileService;

    @PostMapping("/upload")
    public JsonResponse<String> upload(@RequestParam("file") MultipartFile file, String entityCode, String permissionCode) throws IOException {
        String fileCode = fileService.upload(getLoginCode(), file, entityCode, permissionCode);
        return JsonResponse.success(fileCode);
    }

    @GetMapping("/list")
    public JsonResponse<List<SysFileInfo>> list(String entityCode, String permissionCode) {
        return JsonResponse.success(fileService.findBy(entityCode, permissionCode));
    }

    @GetMapping("/get/{code}")
    public JsonResponse<SysFileInfo> get(@PathVariable String code) {
        return JsonResponse.success(fileService.findBy(code));
    }

    @PostMapping("/delete/{code}")
    public JsonResponse<Object> delete(@PathVariable String code, String permissionCode) {
        fileService.delete(getLoginCode(), permissionCode, code);
        return JsonResponse.success();
    }

    @PostMapping("/modify/{code}")
    public JsonResponse<Object> modify(@PathVariable String code, String permissionCode, String name) {
        fileService.modify(getLoginCode(), permissionCode, code, name);
        return JsonResponse.success();
    }

    @GetMapping("/download/{code}")
    public void download(@PathVariable String code, HttpServletResponse response) {
        fileService.download(response, code);
    }

}
