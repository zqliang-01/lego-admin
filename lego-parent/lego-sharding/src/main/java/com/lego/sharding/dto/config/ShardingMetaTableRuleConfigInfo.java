package com.lego.sharding.dto.config;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingMetaTableRuleConfigInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String logicTableName;
    private String actualDataNodes;
    private String shardingColumn;
    private String algorithmCode;
    private String dataSourceCode;
    private String templateCode;
}
