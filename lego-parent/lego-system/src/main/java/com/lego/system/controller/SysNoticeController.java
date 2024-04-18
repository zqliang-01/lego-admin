package com.lego.system.controller;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysNoticeInfo;
import com.lego.system.service.ISysNoticeService;
import com.lego.system.vo.SysNoticeSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/back-end/sys-notice")
public class SysNoticeController extends BaseController {

    @Autowired
    private ISysNoticeService noticeService;

    @PostMapping("/list")
    public JsonResponse<LegoPage<SysNoticeInfo>> list(@RequestBody SysNoticeSearchVO vo) {
        return JsonResponse.success(noticeService.findBy(getLoginCode(), vo));
    }

    @PostMapping("/read/{code}")
    public JsonResponse<Object> read(@PathVariable String code) {
        noticeService.read(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/readAll")
    public JsonResponse<Object> readAll(String type) {
        noticeService.readAll(getLoginCode(), type);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    public JsonResponse<Object> delete(@PathVariable String code) {
        noticeService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/deleteAll")
    public JsonResponse<Object> deleteAll(String type) {
        noticeService.deleteAll(getLoginCode(), type);
        return JsonResponse.success();
    }
}
