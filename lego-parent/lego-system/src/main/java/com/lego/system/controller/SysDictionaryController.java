package com.lego.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.service.ISysDictionaryService;
import com.lego.system.service.ISysSimpleTypeService;

@RestController
@RequestMapping("/back-end/manage-dict")
public class SysDictionaryController extends BaseController {

	@Autowired
	private ISysSimpleTypeService simpleTypeService;

	@Autowired
	private ISysDictionaryService dictionaryService;

    @GetMapping("/list-type")
    public JsonResponse<List<TypeInfo>> listDictionaryType() {
        return JsonResponse.success(simpleTypeService.findDictionaryType());
    }

    @GetMapping("/list")
    public JsonResponse<List<TypeInfo>> list(String typeCode) {
        return JsonResponse.success(dictionaryService.findByType(typeCode));
    }

}
