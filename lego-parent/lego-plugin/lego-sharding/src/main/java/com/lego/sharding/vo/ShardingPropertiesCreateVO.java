package com.lego.sharding.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ShardingPropertiesCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String configCode;
    private Long entityId;
    private String templateCode;
    private Map<String, String> properties = new HashMap<String, String>();
}
