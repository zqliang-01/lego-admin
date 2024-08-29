package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FlowableTaskFormDetailInfo extends DTO {

    private String id;
    private String name;
    private String formKey;
    private String code;
    private boolean finished;
    private Map<String, Object> variables = new HashMap<>();
    private List<FlowableCommentInfo> comments = new ArrayList<>();

    public FlowableTaskFormDetailInfo(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
