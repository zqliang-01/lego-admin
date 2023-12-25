package com.lego.sharding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.sharding.service.IShardingPropertiesService;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;

@RestController
@RequestMapping("/back-end/sharding-properties")
public class ShardingPropertiesController extends BaseController {

	@Autowired
	private IShardingPropertiesService propertiesService;

    @GetMapping("/list")
    public JsonResponse<List<TypeInfo>> listProperties(String entityCode, String templateCode, String configCode) {
        return JsonResponse.success(propertiesService.findBy(entityCode, templateCode, configCode));
    }

    @PostMapping("/add")
    public JsonResponse<Object> add(@RequestBody ShardingPropertiesCreateVO vo) {
    	propertiesService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
