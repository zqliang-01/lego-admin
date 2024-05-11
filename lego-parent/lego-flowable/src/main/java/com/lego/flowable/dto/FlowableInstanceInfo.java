package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowableInstanceInfo extends DTO {

    private String id;
    private String name;
    private TypeInfo status;
    private TypeInfo startUser;
    private Date startTime;
    private Date endTime;
    private String duration;
    private String version;
}
