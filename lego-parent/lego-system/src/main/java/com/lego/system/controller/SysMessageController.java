package com.lego.system.controller;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysMessageCountInfo;
import com.lego.system.dto.SysMessageInfo;
import com.lego.system.service.ISysMessageService;
import com.lego.system.vo.SysMessageSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/back-end/sys-message")
public class SysMessageController extends BaseController {

    @Autowired
    private ISysMessageService messageService;

    @GetMapping("/unreadCount")
    public JsonResponse<SysMessageCountInfo> unreadCount(String type) {
        return JsonResponse.success(messageService.findUnreadCountBy(getLoginCode()));
    }

    @PostMapping("/list")
    public JsonResponse<LegoPage<SysMessageInfo>> list(@RequestBody SysMessageSearchVO vo) {
        return JsonResponse.success(messageService.findBy(getLoginCode(), vo));
    }

    @PostMapping("/read/{code}")
    public JsonResponse<Object> read(@PathVariable String code) {
        messageService.read(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/readAll")
    public JsonResponse<Object> readAll(String type) {
        messageService.readAll(getLoginCode(), type);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    public JsonResponse<Object> delete(@PathVariable String code) {
        messageService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/deleteAll")
    public JsonResponse<Object> deleteAll(String type) {
        messageService.deleteAll(getLoginCode(), type);
        return JsonResponse.success();
    }
}
