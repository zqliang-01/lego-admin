package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class FlowableTaskFormDetailInfo extends DTO {

    private String id;
    private String name;
    private String formKey;
    private String code;
    private String comment;
    private Map<String, Object> variables = new HashMap<>();

}
