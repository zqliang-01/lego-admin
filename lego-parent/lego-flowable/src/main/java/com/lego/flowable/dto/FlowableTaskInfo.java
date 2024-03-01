package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowableTaskInfo extends DTO {

    private String id;
    private String name;
    private String definitionName;
    private String key;
    private TypeInfo assignee;
    private TypeInfo owner;
    private TypeInfo startUser;
    private String formCode;
    private String instanceId;
    private Date createTime;
    private Date endTime;
    private String duration;
}
