package com.lego.crm.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmContractCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private Date startTime;
    private Date endTime;
    private String ownerCode;
    private BigDecimal amount;
    private String lead;
    private String customer;
    private String type;
}