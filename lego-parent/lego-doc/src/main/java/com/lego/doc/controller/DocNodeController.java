package com.lego.doc.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.doc.dto.DocNodeDetailInfo;
import com.lego.doc.dto.DocNodeInfo;
import com.lego.doc.service.IDocNodeService;
import com.lego.doc.vo.DocNodeCreateVO;
import com.lego.doc.vo.DocNodeModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/doc-node")
public class DocNodeController extends BaseController {

    @Autowired
    private IDocNodeService nodeService;

    @GetMapping("/list")
    public JsonResponse<List<DocNodeInfo>> list(String bookCode, String type) {
        return JsonResponse.success(nodeService.findBy(bookCode, type, getLoginCode()));
    }

    @GetMapping("/list-children")
    public JsonResponse<List<DocNodeDetailInfo>> listChildren(String code, String type) {
        return JsonResponse.success(nodeService.findChildrenBy(code, type, getLoginCode()));
    }

    @GetMapping("/get/{code}")
    public JsonResponse<DocNodeDetailInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(nodeService.findBy(getLoginCode(), code));
    }

    @PostMapping("/add")
    public JsonResponse<Object> add(@RequestBody DocNodeCreateVO vo) {
        nodeService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    public JsonResponse<Object> update(@RequestBody DocNodeModifyVO vo) {
        nodeService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/disable/{code}")
    public JsonResponse<Object> disable(@PathVariable String code) {
        nodeService.disable(getLoginCode(), code);
        return JsonResponse.success();
    }

}
