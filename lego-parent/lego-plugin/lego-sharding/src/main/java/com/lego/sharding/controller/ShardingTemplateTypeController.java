package com.lego.sharding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.sharding.dto.ShardingTemplateTypeInfo;
import com.lego.sharding.service.IShardingTemplateTypeService;

@RestController
@RequestMapping("/back-end/sharding-template-type")
public class ShardingTemplateTypeController extends BaseController {

	@Autowired
	private IShardingTemplateTypeService templateTypeService;

    @GetMapping("/list")
    public JsonResponse<List<ShardingTemplateTypeInfo>> listSimpleType() {
        return JsonResponse.success(templateTypeService.findValid());
    }

}
