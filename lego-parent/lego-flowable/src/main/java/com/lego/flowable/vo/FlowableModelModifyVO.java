package com.lego.flowable.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowableModelModifyVO extends VO {

    private String id;
    private String name;
    private String description;
    private String category;
}
