package com.lego.system.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.service.ISysColumnSortService;
import com.lego.system.service.ISysCustomFieldService;
import com.lego.system.vo.SysColumnSortModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-column-sort")
public class SysColumnSortController extends BaseController {

    @Autowired
    private ISysColumnSortService columnSortService;

    @Autowired
    private ISysCustomFieldService customFieldService;

    @GetMapping("/list")
    public JsonResponse<List<SysColumnSortInfo>> list(String formCode) {
        List<SysColumnSortInfo> columnSortInfos = columnSortService.findBy(formCode, getLoginCode());
        List<String> fieldCodes = customFieldService.findCodesByForm(formCode);
        if (columnSortInfos.size() != fieldCodes.size()) {
            columnSortService.updateBy(formCode, getLoginCode(), fieldCodes);
            columnSortInfos = columnSortService.findBy(formCode, getLoginCode());
        }
        return JsonResponse.success(columnSortInfos);
    }

    @PostMapping("/modify-all")
    public JsonResponse<Object> modifyAll(@RequestBody List<SysColumnSortModifyVO> vos) {
        columnSortService.update(getLoginCode(), vos);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    public JsonResponse<Object> modify(@RequestBody SysColumnSortModifyVO vo) {
        columnSortService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
