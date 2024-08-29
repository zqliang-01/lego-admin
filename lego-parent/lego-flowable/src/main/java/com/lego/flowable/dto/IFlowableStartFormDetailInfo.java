package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IFlowableStartFormDetailInfo extends DTO {

    private String formKey;
    private String code;

}
