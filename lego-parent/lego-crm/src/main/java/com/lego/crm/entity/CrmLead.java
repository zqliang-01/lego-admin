package com.lego.crm.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BusiEntity;
import com.lego.crm.entity.CrmDictionary;
import com.lego.crm.entity.CrmCustomer;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "crm_lead")
public class CrmLead extends BusiEntity {

    private String mobile;
    private BigDecimal amount;
    private Date brithday;
    private String address;
    private int age;
    private BigDecimal size;
    private boolean status;
    private String email;
    private String employee;
    private String dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private CrmDictionary source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CrmCustomer customer;

    protected CrmLead() { }

    public CrmLead(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("手机号码", StringUtil.toString(mobile));
        attributes.put("金额", StringUtil.toString(amount));
        attributes.put("生日", DateUtil.toDateString(brithday));
        attributes.put("地址", StringUtil.toString(address));
        attributes.put("年龄", StringUtil.toString(age));
        attributes.put("百分数", StringUtil.toString(size));
        attributes.put("状态", status ? "是" : "否");
        attributes.put("邮箱", StringUtil.toString(email));
        attributes.put("员工", StringUtil.toString(employee));
        attributes.put("部门", StringUtil.toString(dept));
        attributes.put("来源", EntityUtil.toString(source));
        attributes.put("客户", EntityUtil.toString(customer));
    }
}
