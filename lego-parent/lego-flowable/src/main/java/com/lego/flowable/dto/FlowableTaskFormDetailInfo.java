package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FlowableTaskFormDetailInfo extends DTO {

    private String id;
    private String name;
    private String formKey;
    private String code;
    private String comment;
    private boolean finished;
    private Map<String, Object> variables = new HashMap<>();

    public FlowableTaskFormDetailInfo(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
