package com.lego.crm.entity;

import com.lego.core.data.hibernate.BusEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "crm_customer")
public class CrmCustomer extends BusEntity {

    private String mobile;
    private String website;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private CrmDictionary type;

    protected CrmCustomer() {
    }

    public CrmCustomer(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("手机号", StringUtil.toString(mobile));
        attributes.put("网址", StringUtil.toString(website));
        attributes.put("邮箱", StringUtil.toString(email));
        attributes.put("类型", EntityUtil.toString(type));
    }
}
