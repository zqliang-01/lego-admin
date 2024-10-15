package com.lego.mobile.entity;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.mobile.entity.simpletype.MobileAppType;
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
@Table(name = "mobile_app_config")
public class MobileAppConfig extends BaseEntity {

    private String appid;
    private String secret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private MobileAppType type;

    protected MobileAppConfig() {
    }

    public MobileAppConfig(String code, String appid, String secret, MobileAppType type) {
        super(code, null);
        this.appid = appid;
        this.secret = secret;
        this.type = type;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("code", this.code);
        attributes.put("外系统ID", StringUtil.toString(this.appid));
        attributes.put("外系统密钥", StringUtil.toString(this.secret));
        attributes.put("应用类型", EntityUtil.toString(this.type));
    }
}
