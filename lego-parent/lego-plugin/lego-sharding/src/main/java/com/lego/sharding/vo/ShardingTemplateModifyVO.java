package com.lego.sharding.vo;


import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingTemplateModifyVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private boolean enable;
    private String description;
    private String json;
    private String type;
}
