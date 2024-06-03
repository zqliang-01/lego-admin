package com.lego.core.data.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lego.core.data.mybatis.mapper.LegoSqlInjector;

@Configuration
public class MybatisConfig {

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor()); // 乐观锁插件
		// DbType：数据库类型(根据类型获取应使用的分页方言)
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 分页插件
		return interceptor;
	}

//	@Bean
	public LegoSqlInjector legoSqlInjector() {
		return new LegoSqlInjector();
	}
}
