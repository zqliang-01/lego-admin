package com.lego.core.feign.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TaskCompletedVO extends VO {

    private String tableCode;
    private Map<String, Object> variable;
}
