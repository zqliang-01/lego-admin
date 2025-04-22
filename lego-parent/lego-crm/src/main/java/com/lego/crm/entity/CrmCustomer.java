package com.lego.crm.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.lego.core.data.hibernate.entity.BusEntity;
import com.lego.core.enums.PublicTypeEnum;
import com.lego.core.vo.ReadableVO;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "crm_customer")
public class CrmCustomer extends BusEntity {

    @Column(name = "mobile")
    private String mobile;
    @Column(name = "website")
    private String website;
    @Column(name = "email")
    private String email;
    @Column(name = "type")
    private String type;

    protected CrmCustomer() { }

    public CrmCustomer(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("手机号", StringUtil.toString(mobile));
        attributes.put("网址", StringUtil.toString(website));
        attributes.put("邮箱", StringUtil.toString(email));
        attributes.put("类型", StringUtil.toString(type), PublicTypeEnum.DICTIONARY);
    }
}