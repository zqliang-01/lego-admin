package com.lego.crm.controller;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.DictionaryTypeVO;
import com.lego.core.vo.DictionaryVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.crm.service.ICrmDictionaryService;
import com.lego.crm.service.ICrmSimpleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/add-type")
    public JsonResponse<Object> addType(@RequestBody DictionaryTypeVO vo) {
        dictionaryService.addType(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update-type")
    public JsonResponse<Object> updateType(@RequestBody DictionaryTypeVO vo) {
        dictionaryService.updateType(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/add")
    public JsonResponse<Object> add(@RequestBody DictionaryVO vo) {
        dictionaryService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    public JsonResponse<Object> update(@RequestBody DictionaryVO vo) {
        dictionaryService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
