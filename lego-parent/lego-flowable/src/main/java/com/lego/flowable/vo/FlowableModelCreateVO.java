package com.lego.flowable.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowableModelCreateVO extends VO {

    private String key;
    private String name;
    private String description;
    private String category;
}
