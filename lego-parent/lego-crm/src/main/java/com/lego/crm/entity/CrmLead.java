package com.lego.crm.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lego.core.data.hibernate.entity.BusEntity;
import com.lego.core.enums.PublicTypeEnum;
import com.lego.crm.entity.CrmCustomer;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "crm_lead")
public class CrmLead extends BusEntity {

    @Column(name = "mobile")
    private String mobile;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "address")
    private String address;
    @Column(name = "age")
    private int age;
    @Column(name = "size")
    private BigDecimal size;
    @Column(name = "status")
    private boolean status;
    @Column(name = "email")
    private String email;
    @Column(name = "employee")
    private String employee;
    @Column(name = "dept")
    private String dept;
    @Column(name = "source")
    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CrmCustomer customer;

    protected CrmLead() { }

    public CrmLead(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("手机号码", StringUtil.toString(mobile));
        attributes.put("金额", StringUtil.toString(amount));
        attributes.put("生日", DateUtil.toDateString(birthday));
        attributes.put("地址", StringUtil.toString(address));
        attributes.put("年龄", StringUtil.toString(age));
        attributes.put("百分数", StringUtil.toString(size));
        attributes.put("状态", status ? "是" : "否");
        attributes.put("邮箱", StringUtil.toString(email));
        attributes.put("员工", StringUtil.toString(employee), PublicTypeEnum.EMPLOYEE);
        attributes.put("部门", StringUtil.toString(dept), PublicTypeEnum.DEPT);
        attributes.put("来源", StringUtil.toString(source), PublicTypeEnum.DICTIONARY);
        attributes.put("客户", EntityUtil.toString(customer));
    }
}