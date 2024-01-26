package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysFlowableTaskInfo extends DTO {

    private String id;
    private String name;
    private String key;
    private String assignee;
    private String owner;
}
