package com.lego.crm.vo;


import com.lego.core.vo.ModifyVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmCustomerModifyVO extends ModifyVO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String mobile;
    private String website;
    private String email;
    private String type;
}
