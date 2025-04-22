package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysPrintTemplateInfo;
import com.lego.system.service.ISysPrintTemplateService;
import com.lego.system.vo.SysPrintTemplateCreateVO;
import com.lego.system.vo.SysPrintTemplateModifyVO;
import com.lego.system.vo.SysPrintTemplatePrintVO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/back-end/sys-print-template")
public class SysPrintTemplateController extends BaseController {

    @Autowired
    private ISysPrintTemplateService printTemplateService;

    @PostMapping("/add")
    @SaCheckPermission("manage_printTemplate_add")
    public JsonResponse<Object> add(@RequestBody SysPrintTemplateCreateVO vo) {
        printTemplateService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("manage_printTemplate_update")
    public JsonResponse<Object> update(@RequestBody SysPrintTemplateModifyVO vo) {
        printTemplateService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/design")
    @SaCheckPermission("manage_printTemplate_update")
    public JsonResponse<Object> design(@RequestBody SysPrintTemplateModifyVO vo) {
        printTemplateService.design(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage_printTemplate_delete")
    public JsonResponse<Object> delete(@PathVariable String code) {
        printTemplateService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("manage_printTemplate_read")
    public JsonResponse<LegoPage<SysPrintTemplateInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(printTemplateService.findPageBy(vo));
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> list(String formCode) {
        return JsonResponse.success(printTemplateService.findSimpleBy(formCode));
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("manage_printTemplate_read")
    public JsonResponse<SysPrintTemplateInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(printTemplateService.findBy(code));
    }

    @PostMapping("/print")
    public JsonResponse<String> print(@RequestBody SysPrintTemplatePrintVO vo) {
        return JsonResponse.success(printTemplateService.print(vo.getCode(), vo.getParams()));
    }

    @PostMapping("/preview")
    public void preview(String content, String fileType, HttpServletResponse response) {
        BusinessException.check(StringUtil.isNotBlank(fileType), "文件类型不能为空！");
        BusinessException.check(Arrays.asList("pdf", "word").contains(fileType), "无效的文件类型[{0}]", fileType);
        String tempFilePath = printTemplateService.preview(content, fileType);
        if (FileUtil.exist(tempFilePath)) {
            File file = FileUtil.file(tempFilePath);
            BufferedInputStream in = null;
            ServletOutputStream out = null;
            try {
                in = FileUtil.getInputStream(file);
                response.setContentType("application/pdf");
                IoUtil.copy(in, response.getOutputStream());
            } catch (Exception ex) {
                log.error("导出错误", ex);
            } finally {
                IoUtil.close(in);
                IoUtil.close(out);
            }
            return;
        }
        JakartaServletUtil.write(response, JsonResponse.success().toJSONString(), "text/plain");
    }

}
