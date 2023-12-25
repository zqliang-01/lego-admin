package com.lego.sharding.vo;


import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingTableCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private boolean enable;
    private String description;
    private String logicTableName;
    private String actualDataNodes;
    private String shardingColumn;
    private String algorithm;
    private String template;
    private String config;
}
