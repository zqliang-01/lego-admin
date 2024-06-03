package com.lego.sharding.controller;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.sharding.service.IShardingPropertiesService;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sharding-properties")
public class ShardingPropertiesController extends BaseController {

    @Autowired
    private IShardingPropertiesService propertiesService;

    @GetMapping("/list")
    public JsonResponse<List<TypeInfo>> listProperties(Long entityId) {
        return JsonResponse.success(propertiesService.findBy(entityId));
    }

    @PostMapping("/add")
    public JsonResponse<Object> add(@RequestBody ShardingPropertiesCreateVO vo) {
        propertiesService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
