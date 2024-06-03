package com.lego.sharding.dto.config;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingMetaAlgorithmInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String configCode;
    private String templateCode;
}
