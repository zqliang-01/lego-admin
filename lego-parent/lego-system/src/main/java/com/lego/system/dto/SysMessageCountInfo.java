package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysMessageCountInfo extends DTO {

    private int all;
    private int flowable;
    private int form;
    private long notice;
}
