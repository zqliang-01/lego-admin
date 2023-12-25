package com.lego.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.test.entity.ResOrder;
import com.lego.test.service.IResOrderService;

@RestController
@RequestMapping("/back-end/order")
public class ResOrderController {

	@Autowired
	private IResOrderService orderService;

    @GetMapping(params = "action=all")
    public Object list(Integer size) {
    	BusinessException.check(size != null, "入参size不能为空！");
		return orderService.lambdaQuery().orderByAsc(ResOrder::getId).page(new Page<ResOrder>(1, size));
    }

    @PostMapping("/save")
    public String save(Long id) {
    	BusinessException.check(id != null, "入参id不能为空！");
		orderService.save(new ResOrder(id, StringUtil.toString(id)));
        return "ok";
    }

}
