package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowableCommentInfo extends DTO {

    private String id;
    private String content;
    private Date createTime;
    private TypeInfo type;
}
