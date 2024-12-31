package com.lego.core.data.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.data.mybatis.mapper.IMapper;
import com.lego.core.vo.PageVO;

public class MybatisService<M extends IMapper<T>, T> extends ServiceImpl<M, T> {

    protected <E extends BaseEntity> QueryChainWrapper<E> query(BaseMapper<E> mapper) {
        return new QueryChainWrapper<E>(mapper);
    }

	protected <E extends BaseEntity> LambdaQueryChainWrapper<E> lambdaQuery(BaseMapper<E> mapper) {
        return new LambdaQueryChainWrapper<E>(mapper);
    }

    public IPage<T> toPage(PageVO page) {
        return new Page<T>(page.getPageIndex(), page.getPageSize());
    }

    @Override
    public LegoLambdaWrapper<T> lambdaQuery() {
    	return new LegoLambdaWrapper<T>(getBaseMapper());
    }

    protected LambdaQueryWrapper<T> lambdaWrapperQuery() {
    	return Wrappers.lambdaQuery();
    }

	protected LambdaUpdateWrapper<T> lambdaWrapperUpdate() {
    	return Wrappers.lambdaUpdate();
    }
}
