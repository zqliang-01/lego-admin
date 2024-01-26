package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysFlowableDefinitionInfo extends DTO {

    private String id;
    private String name;
    private String key;
    private String category;
    private Integer version;
    private String deploymentId;
    private Boolean suspended;
}
