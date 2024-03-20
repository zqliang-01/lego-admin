package com.lego.sharding.dto.config;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingMetaDataSourceInfo extends DTO {

    private Long id;
    private String code;
    private String name;
}
