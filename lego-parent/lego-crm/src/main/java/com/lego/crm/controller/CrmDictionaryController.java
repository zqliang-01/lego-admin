package com.lego.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.crm.service.ICrmDictionaryService;
import com.lego.crm.service.ICrmSimpleTypeService;

@RestController
@RequestMapping("/back-end/crm-dict")
public class CrmDictionaryController extends BaseController {

	@Autowired
	private ICrmSimpleTypeService simpleTypeService;

	@Autowired
	private ICrmDictionaryService dictionaryService;

    @GetMapping("/list-type")
    public JsonResponse<List<TypeInfo>> listDictionaryType() {
        return JsonResponse.success(simpleTypeService.findDictionaryType());
    }

    @GetMapping("/list")
    public JsonResponse<List<TypeInfo>> list(String typeCode) {
        return JsonResponse.success(dictionaryService.findByType(typeCode));
    }

}
