package com.lego.doc.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.doc.dto.DocBookInfo;
import com.lego.doc.service.IDocBookService;
import com.lego.doc.vo.DocBookCreateVO;
import com.lego.doc.vo.DocBookModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/doc-book")
public class DocBookController extends BaseController {

    @Autowired
    private IDocBookService bookService;

    @PostMapping("/add")
    @SaCheckPermission("doc_book_add")
    public JsonResponse<Object> add(@RequestBody DocBookCreateVO vo) {
        bookService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("doc_book_update")
    public JsonResponse<Object> update(@RequestBody DocBookModifyVO vo) {
        bookService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("doc_book_read")
    public JsonResponse<LegoPage<DocBookInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(bookService.findPageBy(getLoginCode(), vo));
    }

    @PostMapping("/list-disable")
    @SaCheckPermission("doc_recycle_read")
    public JsonResponse<LegoPage<DocBookInfo>> listDisable(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(bookService.findDisablePageBy(getLoginCode(), vo));
    }

    @PostMapping("/list-public")
    public JsonResponse<LegoPage<DocBookInfo>> listPublic(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(bookService.findPublicPageBy(getLoginCode(), vo));
    }

    @GetMapping("/get/{code}")
    public JsonResponse<DocBookInfo> get(@PathVariable String code) {
        return JsonResponse.success(bookService.findBy(getLoginCode(), code));
    }

    @PostMapping("/disable/{code}")
    @SaCheckPermission("doc_book_delete")
    public JsonResponse<Object> disable(@PathVariable String code) {
        bookService.disable(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/enable/{code}")
    @SaCheckPermission("doc_recycle_enable")
    public JsonResponse<Object> enable(@PathVariable String code) {
        bookService.enable(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("doc_recycle_delete")
    public JsonResponse<Object> delete(@PathVariable String code) {
        bookService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

}
