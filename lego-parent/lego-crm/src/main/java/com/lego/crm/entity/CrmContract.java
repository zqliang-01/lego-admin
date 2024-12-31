package com.lego.crm.entity;

import java.math.BigDecimal;
import com.lego.core.data.hibernate.entity.AddressEntity;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lego.core.data.hibernate.entity.BusEntity;
import com.lego.core.enums.PublicTypeEnum;
import com.lego.crm.entity.CrmCustomer;
import com.lego.crm.entity.CrmLead;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "crm_contract")
public class CrmContract extends BusEntity {

    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "owner")
    private String owner;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "type")
    private String type;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "province", column = @Column(name = "province")),
        @AttributeOverride(name = "city", column = @Column(name = "city")),
        @AttributeOverride(name = "area", column = @Column(name = "area")),
        @AttributeOverride(name = "detail", column = @Column(name = "detail"))
    })
    private AddressEntity address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private CrmLead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CrmCustomer customer;

    protected CrmContract() { }

    public CrmContract(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("开始时间", DateUtil.toDateTimeString(startTime));
        attributes.put("结束时间", DateUtil.toDateTimeString(endTime));
        attributes.put("负责人", StringUtil.toString(owner), PublicTypeEnum.EMPLOYEE);
        attributes.put("合同金额", StringUtil.toString(amount));
        attributes.put("类型", StringUtil.toString(type), PublicTypeEnum.DICTIONARY);
        attributes.put("地址", StringUtil.toString(address));
        attributes.put("线索", EntityUtil.toString(lead));
        attributes.put("客户", EntityUtil.toString(customer));
    }
}