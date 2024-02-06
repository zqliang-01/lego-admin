package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowableTaskInfo extends DTO {

    private String id;
    private String name;
    private String key;
    private String assignee;
    private String owner;
    private String formCode;
    private String instanceId;
    private Date createTime;
    private Date endTime;
}
