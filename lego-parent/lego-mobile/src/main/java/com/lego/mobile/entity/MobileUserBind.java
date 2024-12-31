package com.lego.mobile.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.mobile.entity.simpletype.MobileAppType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "mobile_user_bind")
public class MobileUserBind extends BaseEntity {

    private String openid;
    private String token;
    private String employeeCode;
    private Date updateTime;
    private Date expiredTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private MobileAppType type;

    protected MobileUserBind() {
    }

    public MobileUserBind(String openid, String employeeCode, MobileAppType type) {
        super(type.getName());
        this.openid = openid;
        this.employeeCode = employeeCode;
        this.type = type;
        this.updateTime = DateUtil.currentDateTime();
        this.expiredTime = DateUtil.addDays(this.updateTime, 30);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("外系统ID", this.openid);
        attributes.put("工号", this.employeeCode);
        attributes.put("到期时间", DateUtil.toDateTimeString(this.expiredTime));
        attributes.put("绑定类型", EntityUtil.toString(this.type));
        attributes.put("临时Token", StringUtil.toString(this.token));
    }

    public boolean isExpired() {
        return DateUtil.ltCurrent(this.expiredTime);
    }

    public void refreshExpired(String token) {
        this.token = StringUtil.getMD5(token);
        this.updateTime = DateUtil.currentDateTime();
        this.expiredTime = DateUtil.addDays(DateUtil.currentDate(), 30);
    }

    public void expired() {
        this.token = "";
        this.expiredTime = DateUtil.currentDateTime();
        this.updateTime = DateUtil.currentDateTime();
    }

    public void setToken(String token) {
        this.token = StringUtil.getMD5(token);
    }
}
