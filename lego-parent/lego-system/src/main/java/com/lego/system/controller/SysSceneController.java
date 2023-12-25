package com.lego.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysSceneInfo;
import com.lego.system.service.ISysSceneService;
import com.lego.system.vo.SysSceneCreateVO;
import com.lego.system.vo.SysSceneModifyVO;
import com.lego.system.vo.SysSceneVisibleVO;

@RestController
@RequestMapping("/back-end/sys-scene")
public class SysSceneController extends BaseController {

	@Autowired
	private ISysSceneService sceneService;

    @GetMapping("/list")
	public JsonResponse<List<SysSceneInfo>> list(String formCode) {
    	return JsonResponse.success(sceneService.findByForm(formCode));
	}

    @GetMapping("/list-visible")
	public JsonResponse<List<SysSceneInfo>> listVisible(String formCode) {
    	return JsonResponse.success(sceneService.findBy(formCode, true));
	}

    @PostMapping("/add")
	public JsonResponse<Object> add(@RequestBody SysSceneCreateVO vo) {
    	sceneService.add(getLoginCode(), vo);
    	return JsonResponse.success();
	}

    @PostMapping("/modify")
    public JsonResponse<Object> modify(@RequestBody SysSceneModifyVO vo) {
    	sceneService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify-visible")
    public JsonResponse<Object> modifyVisible(@RequestBody SysSceneVisibleVO vo) {
    	sceneService.modifyVisible(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
