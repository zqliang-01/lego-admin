package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FlowableTaskFormDetailInfo extends DTO {

    private String id;
    private String formKey;
    private String code;
    private String comment;
}
