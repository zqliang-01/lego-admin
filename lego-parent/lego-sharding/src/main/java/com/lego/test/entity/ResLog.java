package com.lego.test.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("res_log")
public class ResLog {

	@TableId(value="id")
    private Long id;
	private String name;
	private Date createTime;

	protected ResLog() { }

	public ResLog(Long id, String name, Date createTime) {
		this.id = id;
		this.name = name;
		this.createTime = createTime;
	}
}
