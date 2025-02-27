package com.lego.crm.vo;


import com.lego.core.vo.CreateVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmCustomerCreateVO extends CreateVO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String mobile;
    private String website;
    private String email;
    private String type;
}