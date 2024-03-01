package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowableDefinitionInfo extends DTO {

    private String id;
    private String name;
    private String key;
    private String category;
    private String version;
    private Date deploymentTime;
    private Boolean active;
}
