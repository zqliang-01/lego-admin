package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.flowable.vo.FlowableCommentType;
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

    public boolean isReject() {
        if (type == null) {
            return false;
        }
        return FlowableCommentType.REJECT.getCode().equals(type.getCode());
    }
}
