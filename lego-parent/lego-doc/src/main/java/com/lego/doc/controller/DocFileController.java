package com.lego.doc.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.doc.service.IDocFileService;
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

@RestController
@RequestMapping("/back-end/doc-file")
public class DocFileController extends BaseController {

    @Autowired
    private IDocFileService fileService;

    @PostMapping("/upload")
    public JsonResponse<String> upload(@RequestParam("file") MultipartFile file, String entityCode, String permissionCode) throws IOException {
        String fileCode = fileService.upload(getLoginCode(), file);
        return JsonResponse.success(fileCode);
    }

    @GetMapping("/download/{code}")
    public void download(@PathVariable String code, HttpServletResponse response) {
        fileService.download(response, code);
    }

}
