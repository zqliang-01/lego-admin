package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SysPrintTemplatePrintVO extends VO {

    private String code;
    private Map<String, String> params = new HashMap<>();
}
