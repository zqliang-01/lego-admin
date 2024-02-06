package com.lego.flowable.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowableDefinitionSearchVO extends PageVO {

    private String name;
    private String key;
}
