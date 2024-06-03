package com.lego.core.data.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.core.vo.PageVO;

public class LegoLambdaWrapper<T> extends LambdaQueryChainWrapper<T> {

	private static final long serialVersionUID = 1L;

	public LegoLambdaWrapper(BaseMapper<T> baseMapper) {
		super(baseMapper);
	}

	@SuppressWarnings("unchecked")
	public <E extends IPage<T>> E page(PageVO pageVO) {
		E page = (E) new Page<T>(pageVO.getPageIndex(), pageVO.getPageSize());
		return super.page(page);
	}
}
