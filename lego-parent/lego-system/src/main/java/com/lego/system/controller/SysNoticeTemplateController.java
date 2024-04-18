package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysNoticeTemplateInfo;
import com.lego.system.service.ISysNoticeTemplateService;
import com.lego.system.vo.SysNoticeTemplateCreateVO;
import com.lego.system.vo.SysNoticeTemplateModifyVO;
import com.lego.system.vo.SysNoticeTemplateSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/back-end/sys-notice-template")
public class SysNoticeTemplateController extends BaseController {

    @Autowired
    private ISysNoticeTemplateService noticeTemplateService;

    @PostMapping("/list")
    @SaCheckPermission("manage_notice_read")
    public JsonResponse<LegoPage<SysNoticeTemplateInfo>> list(@RequestBody SysNoticeTemplateSearchVO vo) {
        return JsonResponse.success(noticeTemplateService.findBy(getLoginCode(), vo));
    }

    @PostMapping("/publish/{code}")
    @SaCheckPermission("manage_notice_publish")
    public JsonResponse<Object> publish(@PathVariable String code) {
        noticeTemplateService.publish(getLoginCode(), code);
        return JsonResponse.success();
    }

    @PostMapping("/add")
    @SaCheckPermission("manage_notice_add")
    public JsonResponse<Object> add(@RequestBody SysNoticeTemplateCreateVO vo) {
        noticeTemplateService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage_notice_add")
    public JsonResponse<Object> modify(@RequestBody SysNoticeTemplateModifyVO vo) {
        noticeTemplateService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
