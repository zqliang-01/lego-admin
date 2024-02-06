package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowableDefinitionInfo extends DTO {

    private String id;
    private String name;
    private String key;
    private String category;
    private String version;
    private String deploymentId;
    private Boolean active;
}
