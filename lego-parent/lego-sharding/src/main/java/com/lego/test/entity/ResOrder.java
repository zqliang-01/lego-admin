package com.lego.test.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lego.core.util.DateUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("res_order")
public class ResOrder {

	@TableId(value="id")
    private Long id;
	private String name;
	private Date createTime;

	protected ResOrder() { }

	public ResOrder(Long id, String name) {
		this.id = id;
		this.name = name;
		this.createTime = DateUtil.currentDateTime();
	}
}
