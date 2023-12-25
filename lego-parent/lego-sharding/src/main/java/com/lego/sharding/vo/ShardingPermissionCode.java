package com.lego.sharding.vo;

public interface ShardingPermissionCode {

	String sharding = "manage:sharding";
	String shardingConfig = "manage:sharding:config";
	String shardingAlgorithm = "manage:sharding:algorithm";
	String shardingDataSource = "manage:sharding:dataSource";
	String shardingTableConfig = "manage:sharding:tableConfig";
	String shardingTemplate = "manage:sharding:template";
}
