package com.lego.system.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.TypeInfo;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysCustomFieldInfo;
import com.lego.system.dto.SysCustomFieldTypeInfo;
import com.lego.system.dto.SysCustomerFormFieldInfo;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.dto.SysTableColumnInfo;
import com.lego.system.service.ISysCustomFieldService;
import com.lego.system.service.ISysCustomFormService;
import com.lego.system.service.ISysGenTableService;
import com.lego.system.vo.SysCustomFormFieldModifyVO;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sys-custom-field")
public class SysCustomFieldController extends BaseController {

	@Autowired
	private ISysCustomFieldService customFieldService;

	@Autowired
	private ISysCustomFormService customFormService;

	@Autowired
	private ISysGenTableService tableService;

    @GetMapping("/list")
    public JsonResponse<SysCustomerFormFieldInfo> list(String formCode, String tableCode) {
    	String appCode = customFormService.findAppCodeBy(formCode);
        List<SysCustomFieldInfo> fieldInfos = customFieldService.findBy(formCode);
        List<TypeInfo> columnInfos = customFieldService.findGenColumnBy(tableCode);
        List<List<SysCustomFieldInfo>> results = new ArrayList<List<SysCustomFieldInfo>>();
        Map<Integer, List<SysCustomFieldInfo>> fieldGroupMap = fieldInfos.stream().collect(Collectors.groupingBy(SysCustomFieldInfo::getXAxis));
        fieldGroupMap.values().stream().forEach(m -> {
        	results.add(m.stream().sorted(Comparator.comparing(SysCustomFieldInfo::getYAxis)).collect(Collectors.toList()));
        });
		return JsonResponse.success(new SysCustomerFormFieldInfo(appCode, results, columnInfos));
    }

    @GetMapping("/list-table-header")
    public JsonResponse<SysTableColumnInfo> listTableHeader(String permissionCode) {
    	String formCode = customFormService.findCodeByPermission(permissionCode);
		return JsonResponse.success(customFieldService.findValidBy(getLoginCode(), formCode));
    }

    @GetMapping("/list-table-header/{formCode}")
    public JsonResponse<SysTableColumnInfo> listTableHeaderByForm(@PathVariable String formCode) {
		return JsonResponse.success(customFieldService.findValidBy(getLoginCode(), formCode));
    }

    @GetMapping("/list-init")
    @SaCheckPermission("manage:customForm:design")
    public JsonResponse<SysCustomerFormFieldInfo> listInit(String formCode, String tableCode) {
    	String appCode = customFormService.findAppCodeBy(formCode);
        List<TypeInfo> columnInfos = customFieldService.findGenColumnBy(tableCode);
        List<List<SysCustomFieldInfo>> results = customFieldService.findInit(appCode, tableCode);
		return JsonResponse.success(new SysCustomerFormFieldInfo(appCode, results, columnInfos));
    }

    @GetMapping("/list-type")
    public JsonResponse<List<SysCustomFieldTypeInfo>> listType(String tableCode) {
    	SysGenTableInfo table = tableService.findByCode(tableCode);
    	List<SysCustomFieldTypeInfo> typeInfos = new ArrayList<SysCustomFieldTypeInfo>();
    	for (CustomFieldTypeEnum type : CustomFieldTypeEnum.values()) {
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
