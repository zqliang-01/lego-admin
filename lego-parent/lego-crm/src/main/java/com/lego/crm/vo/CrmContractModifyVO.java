package com.lego.crm.vo;

import java.math.BigDecimal;
import com.lego.core.data.hibernate.entity.AddressEntity;
import java.util.Date;

import com.lego.core.vo.ModifyVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmContractModifyVO extends ModifyVO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private Date startTime;
    private Date endTime;
    private String owner;
    private BigDecimal amount;
    private String type;
    private AddressEntity address;
    private String lead;
    private String customer;
}