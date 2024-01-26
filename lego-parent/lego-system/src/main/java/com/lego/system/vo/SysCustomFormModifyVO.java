package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCustomFormModifyVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String table;
    private boolean enable;
    private String queryApiUrl;
    private String detailApiUrl;
    private String addApiUrl;
    private String updateApiUrl;
    private String deleteApiUrl;
    private String exportApiUrl;
    private String exportAllApiUrl;
}
