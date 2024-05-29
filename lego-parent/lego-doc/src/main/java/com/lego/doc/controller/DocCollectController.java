package com.lego.doc.controller;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.doc.dto.DocCollectInfo;
import com.lego.doc.service.IDocCollectService;
import com.lego.doc.vo.DocCollectSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/doc-collect")
public class DocCollectController extends BaseController {

    @Autowired
    private IDocCollectService collectService;

    @PostMapping("/add")
    public JsonResponse<String> add(String nodeCode) {
        return JsonResponse.success(collectService.add(getLoginCode(), nodeCode));
    }

    @GetMapping("/get-code")
    public JsonResponse<String> getCode(String nodeCode) {
        return JsonResponse.success(collectService.findCodeBy(getLoginCode(), nodeCode));
    }

    @PostMapping("/list")
    public JsonResponse<LegoPage<DocCollectInfo>> list(@RequestBody DocCollectSearchVO vo) {
        return JsonResponse.success(collectService.findPageBy(getLoginCode(), vo));
    }

    @PostMapping("/delete/{code}")
    public JsonResponse<Object> delete(@PathVariable String code) {
        collectService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

}
