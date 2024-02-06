package com.lego.flowable.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

@Getter
@Setter
public class FlowableTaskStartVO extends VO {

    private String definitionId;
    private Map<String, Object> variables = new HashedMap<>();
}
