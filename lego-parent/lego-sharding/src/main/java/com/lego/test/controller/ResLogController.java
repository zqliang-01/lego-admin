package com.lego.test.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.core.data.IdGenerator;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import com.lego.test.entity.ResLog;
import com.lego.test.service.IResLogService;

@RestController
@RequestMapping("/back-end/log")
public class ResLogController {

	@Autowired
	private IResLogService logService;

    @GetMapping(params = "action=all")
    public Object list(Integer size) {
    	BusinessException.check(size != null, "入参size不能为空！");
		return logService.lambdaQuery().orderByAsc(ResLog::getId).page(new Page<ResLog>(1, size));
    }

    @PostMapping("/save")
    public String save(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")Date createTime) {
    	if (createTime == null) {
    		createTime = DateUtil.currentDateTime();
    	}
    	Long id = IdGenerator.getCurrent().nextId("general");
		logService.save(new ResLog(id, StringUtil.toString(id), createTime));
        return "ok";
    }

}
