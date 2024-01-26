package com.lego.crm.entity;

import com.lego.core.data.hibernate.BusEntity;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "crm_contract")
public class CrmContract extends BusEntity {

    private Date startTime;
    private Date endTime;
    private String ownerCode;
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private CrmLead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CrmCustomer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private CrmDictionary type;

    protected CrmContract() {
    }

    public CrmContract(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("开始时间", DateUtil.toDateString(startTime));
        attributes.put("结束时间", DateUtil.toDateString(endTime));
        attributes.put("负责人", StringUtil.toString(ownerCode));
        attributes.put("合同金额", StringUtil.toString(amount));
        attributes.put("线索", EntityUtil.toString(lead));
        attributes.put("客户", EntityUtil.toString(customer));
        attributes.put("类型", EntityUtil.toString(type));
    }
}
