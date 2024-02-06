package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.lego.core.common.Constants;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysCodePreviewInfo;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.service.ISysGenTableColumnService;
import com.lego.system.service.ISysGenTableService;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.util.VelocityUtil;
import com.lego.system.vo.SysGenTableCreateVO;
import com.lego.system.vo.SysGenTableModifyVO;
import com.lego.system.vo.SysGenTableSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/back-end/sys-gen-table")
public class SysGenTableController extends BaseController {

    @Autowired
    private ISysGenTableService tableService;

    @Autowired
    private ISysGenTableColumnService columnService;

    @Autowired
    private ISysPermissionService permissionService;

    @PostMapping("/list")
    @SaCheckPermission("manage_genTable_read")
    public JsonResponse<LegoPage<SysGenTableInfo>> list(SysGenTableSearchVO vo) {
        return JsonResponse.success(tableService.findPageBy(vo));
    }

    @GetMapping("/list-not-exists")
    public JsonResponse<List<TypeInfo>> listNotExists() {
        return JsonResponse.success(tableService.findNotExists());
    }

    @GetMapping("/list-all")
    public JsonResponse<List<TypeInfo>> listAll() {
        return JsonResponse.success(tableService.findAll());
    }

    @GetMapping("/list-name")
    public JsonResponse<List<TypeInfo>> listName() {
        return JsonResponse.success(tableService.findTableName());
    }

    @GetMapping("/get-init/{code}")
    @SaCheckPermission("manage_genTable_update")
    public JsonResponse<SysGenTableInfo> getInit(@PathVariable String code) {
        return JsonResponse.success(tableService.findInitBy(code));
    }

    @PostMapping("/add")
    @SaCheckPermission("manage_genTable_update")
    public JsonResponse<Object> add(@RequestBody SysGenTableCreateVO vo) {
        tableService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage_genTable_update")
    public JsonResponse<Object> modify(@RequestBody SysGenTableModifyVO vo) {
        tableService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/sync/{code}")
    @SaCheckPermission("manage_genTable_sync")
    public JsonResponse<Object> sync(@PathVariable String code) {
        tableService.sync(getLoginCode(), code);
        return JsonResponse.success();
    }

    @GetMapping("/preview/{code}")
    @SaCheckPermission("manage_genTable_read")
    public JsonResponse<List<SysCodePreviewInfo>> preview(@PathVariable String code) {
        SysGenTableInfo table = tableService.findByCode(code);
        int sn = permissionService.findMaxSn(table.getAppCode());
        List<SysGenTableColumnInfo> columns = columnService.findByTable(code);
        VelocityContext context = VelocityUtil.prepareContext(sn / 10 + 1, table, columns);

        SysCodePreviewInfo rootPath = new SysCodePreviewInfo("root");
        for (TypeInfo template : VelocityUtil.getTemplateList()) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template.getName(), Constants.DEFAULT_CHARSET_NAME);
            tpl.merge(context, sw);
            VelocityUtil.addTemplateCode(rootPath, VelocityUtil.getFileName(template, table), sw.toString());
        }
        return JsonResponse.success(rootPath.getChildrens());
    }

    @GetMapping("/download/{code}")
    @SaCheckPermission("manage_genTable_read")
    public void download(@PathVariable String code, HttpServletResponse response) throws IORuntimeException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        SysGenTableInfo table = tableService.findByCode(code);
        int sn = permissionService.findMaxSn(table.getAppCode());
        List<SysGenTableColumnInfo> columns = columnService.findByTable(code);
        VelocityContext context = VelocityUtil.prepareContext(sn / 10 + 1, table, columns);

        for (TypeInfo template : VelocityUtil.getTemplateList()) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template.getName(), Constants.DEFAULT_CHARSET_NAME);
            tpl.merge(context, sw);
            try {
                zip.putNextEntry(new ZipEntry(VelocityUtil.getFileName(template, table)));
                IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
                IoUtil.close(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getCode(), e);
            }
        }
        IoUtil.close(zip);
        byte[] data = outputStream.toByteArray();
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"lego_gen_code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }

}
