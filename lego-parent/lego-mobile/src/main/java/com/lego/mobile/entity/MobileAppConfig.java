package com.lego.mobile.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.mobile.entity.simpletype.MobileAppType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("code", this.code);
        attributes.put("外系统ID", StringUtil.toString(this.appid));
        attributes.put("外系统密钥", StringUtil.toString(this.secret));
        attributes.put("应用类型", EntityUtil.toString(this.type));
    }
}
