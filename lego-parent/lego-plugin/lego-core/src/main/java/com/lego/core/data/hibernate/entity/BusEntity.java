package com.lego.core.data.hibernate.entity;

import com.lego.core.data.hibernate.jpa.IJpaFilterName;
import com.lego.core.module.flowable.FlowableCheckStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Filter(name = IJpaFilterName.CREATOR_CODE, condition = "creator_code IN (:filterCodes)")
@Filter(name = IJpaFilterName.DEPT_CODE, condition = "dept_code IN (:filterCodes)")
public class BusEntity extends BaseEntity {

    @Getter
    @Setter
    private Date updateTime;
    @Getter
    @Setter
    private String creatorCode;
    @Getter
    @Setter
    private String checkStatus;

    protected BusEntity() {
    }

    protected BusEntity(String name) {
        this(null, name);
    }

    protected BusEntity(String code, String name) {
        super(code, name);
        this.checkStatus = FlowableCheckStatus.completed;
    }

    public void updateCheckStatus(String checkStatus) {
        if (!FlowableCheckStatus.completed.equals(this.checkStatus)) {
            this.checkStatus = checkStatus;
        }
    }
}
