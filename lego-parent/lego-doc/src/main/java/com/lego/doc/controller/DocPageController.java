package com.lego.doc.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.doc.dto.DocPageInfo;
import com.lego.doc.service.IDocPageService;
import com.lego.doc.vo.DocPageModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/doc-page")
public class DocPageController extends BaseController {

    @Autowired
    private IDocPageService pageService;

    @PostMapping("/update")
    public JsonResponse<Object> update(@RequestBody DocPageModifyVO vo) {
        pageService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @GetMapping("/get/{code}")
    public JsonResponse<DocPageInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(pageService.findBy(getLoginCode(), code));
    }

}
