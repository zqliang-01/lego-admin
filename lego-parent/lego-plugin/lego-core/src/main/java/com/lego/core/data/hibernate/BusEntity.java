package com.lego.core.data.hibernate;

import com.lego.core.flowable.FlowableCheckStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class BusEntity extends BaseEntity {

    @Getter
    @Setter
    private Date updateTime;
    @Getter
    @Setter
    private String creatorCode;
    @Getter
    private String checkStatus;

    protected BusEntity() {
    }

    protected BusEntity(String name) {
        this(null, name);
    }

    protected BusEntity(String code, String name) {
        super(code, name);
        this.checkStatus = FlowableCheckStatus.checking;
    }

    public void updateCheckStatus(String checkStatus) {
        if (!FlowableCheckStatus.completed.equals(this.checkStatus)) {
            this.checkStatus = checkStatus;
        }
    }
}
