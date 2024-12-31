package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SysGenTableColumnModifyVO extends VO {

    private static final long serialVersionUID = 1L;

    private int sn;
    private String code;
    private String name;
    private String comment;
    private String dataType;
    private String formType;
    private String javaField;
    private String javaFieldType;
    private String relativeTable;
    private boolean required;
    private boolean unique;
    private Map<String, String> attributes = new HashMap<>();
}
