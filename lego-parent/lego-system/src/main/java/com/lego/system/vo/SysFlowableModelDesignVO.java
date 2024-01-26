package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysFlowableModelDesignVO extends VO {

    private String key;
    private String bpmnXml;
}
