package com.lego.core.data.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface IMapper<T> extends BaseMapper<T> {

	T selectByCode(String code);

	T selectAll();
}
