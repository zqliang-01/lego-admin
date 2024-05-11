package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysCustomFieldInfo;
import com.lego.system.dto.SysCustomFieldTypeInfo;
import com.lego.system.dto.SysCustomFormCreateInfo;
import com.lego.system.dto.SysCustomFormDetailInfo;
import com.lego.system.dto.SysCustomFormFieldInfo;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.dto.SysCustomFormListInfo;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.service.ISysCustomFieldService;
import com.lego.system.service.ISysCustomFormService;
import com.lego.system.service.ISysGenTableColumnService;
import com.lego.system.service.ISysGenTableService;
import com.lego.system.vo.SysCustomFormFieldModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/back-end/sys-custom-field")
public class SysCustomFieldController extends BaseController {

    @Autowired
    private ISysCustomFieldService customFieldService;

    @Autowired
    private ISysCustomFormService customFormService;

    @Autowired
    private ISysGenTableService tableService;

    @Autowired
    private ISysGenTableColumnService tableColumnService;

    @GetMapping("/list")
    public JsonResponse<SysCustomFormFieldInfo> list(String formCode) {
        SysCustomFormInfo formInfo = customFormService.findBy(formCode);
        BusinessException.check(formInfo.getTable() != null, "表单未关联数据表！");

        List<List<SysCustomFieldInfo>> results = new ArrayList<List<SysCustomFieldInfo>>();
        List<SysCustomFieldInfo> fieldInfos = customFieldService.findBy(formCode);
        Map<Integer, List<SysCustomFieldInfo>> fieldGroupMap = fieldInfos.stream().collect(Collectors.groupingBy(SysCustomFieldInfo::getXAxis));
        fieldGroupMap.values().stream().forEach(m -> {
            results.add(m.stream().sorted(Comparator.comparing(SysCustomFieldInfo::getYAxis)).collect(Collectors.toList()));
        });

        TypeInfo table = formInfo.getTable();
        String tableCode = table.getCode();
        String tableName = table.getName();
        SysGenTableInfo tableInfo = tableService.findByCode(tableCode);
        List<TypeInfo> columnInfos = tableColumnService.findSimpleTypeBy(tableCode);
        return JsonResponse.success(new SysCustomFormFieldInfo(tableInfo.getAppCode(), tableName, columnInfos, results));
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> listSimple(String formCode) {
        return JsonResponse.success(customFieldService.findSimpleTypeBy(formCode));
    }

    @GetMapping("/list-table-header/{formCode}")
    public JsonResponse<SysCustomFormListInfo> listTableHeaderByForm(@PathVariable String formCode) {
        SysCustomFormInfo formInfo = customFormService.findBy(formCode);
        List<SysCustomFieldInfo> results = customFieldService.findValidBy(getLoginCode(), formCode);
        return JsonResponse.success(new SysCustomFormListInfo(formInfo, results));
    }

    @GetMapping("/list-detail/{formCode}")
    public JsonResponse<SysCustomFormDetailInfo> listDetail(@PathVariable String formCode) {
        SysCustomFormInfo formInfo = customFormService.findBy(formCode);
        List<SysCustomFieldInfo> results = customFieldService.findValidBy(getLoginCode(), formCode);
        return JsonResponse.success(new SysCustomFormDetailInfo(formInfo, results));
    }

    @GetMapping("/list-create/{formCode}")
    public JsonResponse<SysCustomFormCreateInfo> listCreate(@PathVariable String formCode) {
        SysCustomFormInfo formInfo = customFormService.findBy(formCode);
        BusinessException.check(formInfo.getTable() != null, "表单未关联数据表！");

        List<List<SysCustomFieldInfo>> results = new ArrayList<List<SysCustomFieldInfo>>();
        List<SysCustomFieldInfo> fieldInfos = customFieldService.findValidBy(getLoginCode(), formCode);
        Map<Integer, List<SysCustomFieldInfo>> fieldGroupMap = fieldInfos.stream().collect(Collectors.groupingBy(SysCustomFieldInfo::getXAxis));
        fieldGroupMap.values().stream().forEach(m -> {
            results.add(m.stream().sorted(Comparator.comparing(SysCustomFieldInfo::getYAxis)).collect(Collectors.toList()));
        });
        return JsonResponse.success(new SysCustomFormCreateInfo(formInfo, results));
    }

    @GetMapping("/list-init")
    @SaCheckPermission("manage_customForm_design")
    public JsonResponse<SysCustomFormFieldInfo> listInit(String formCode) {
        SysCustomFormInfo formInfo = customFormService.findBy(formCode);
        BusinessException.check(formInfo.getTable() != null, "表单未关联数据表！");

        TypeInfo table = formInfo.getTable();
        String tableCode = table.getCode();
        String tableName = table.getName();
        SysGenTableInfo tableInfo = tableService.findByCode(tableCode);
        List<TypeInfo> columnInfos = tableColumnService.findSimpleTypeBy(tableCode);
        List<List<SysCustomFieldInfo>> results = customFieldService.findInit(formInfo.getTable().getCode());
        return JsonResponse.success(new SysCustomFormFieldInfo(tableInfo.getAppCode(), tableName, columnInfos, results));
    }

    @GetMapping("/list-type")
    public JsonResponse<List<SysCustomFieldTypeInfo>> listType(String tableCode) {
        SysGenTableInfo table = tableService.findByCode(tableCode);
        List<SysCustomFieldTypeInfo> typeInfos = new ArrayList<SysCustomFieldTypeInfo>();
        for (CustomFieldTypeEnum type : CustomFieldTypeEnum.values()) {
            if (type == CustomFieldTypeEnum.CHECKBOX) {
                continue;
            }
            String javaField = type.getType().getSimpleName();
            if (type == CustomFieldTypeEnum.SELECT) {
                String appCode = StringUtil.toFirstUpper(table.getAppCode());
                javaField = table.getPackageName() + ".entity." + appCode + "Dictionary";
            }
            typeInfos.add(new SysCustomFieldTypeInfo(type.getCode(), type.getName(), javaField));
        }
        return JsonResponse.success(typeInfos);
    }

    @PostMapping("/modify")
    public JsonResponse<Object> modify(@RequestBody SysCustomFormFieldModifyVO vo) {
        customFormService.modifyField(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
