package com.lego.system.controller;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysDictionaryInfo;
import com.lego.system.service.ISysDictionaryService;
import com.lego.system.service.ISysSimpleTypeService;
import com.lego.system.vo.SysDictionaryTypeVO;
import com.lego.system.vo.SysDictionaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-dict")
public class SysDictionaryController extends BaseController {

    @Autowired
    private ISysSimpleTypeService simpleTypeService;

    @Autowired
    private ISysDictionaryService dictionaryService;

    @GetMapping("/list-type")
    public JsonResponse<List<TypeInfo>> listDictionaryType() {
        return JsonResponse.success(simpleTypeService.findDictionaryType());
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> listSimple(String typeCode) {
        return JsonResponse.success(dictionaryService.findSimpleByType(typeCode));
    }

    @GetMapping("/list")
    public JsonResponse<List<SysDictionaryInfo>> list(String typeCode) {
        return JsonResponse.success(dictionaryService.findByType(typeCode));
    }

    @PostMapping("/add-type")
    public JsonResponse<Object> addType(@RequestBody SysDictionaryTypeVO vo) {
        dictionaryService.addType(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update-type")
    public JsonResponse<Object> updateType(@RequestBody SysDictionaryTypeVO vo) {
        dictionaryService.updateType(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/add")
    public JsonResponse<Object> add(@RequestBody SysDictionaryVO vo) {
        dictionaryService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    public JsonResponse<Object> update(@RequestBody SysDictionaryVO vo) {
        dictionaryService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
