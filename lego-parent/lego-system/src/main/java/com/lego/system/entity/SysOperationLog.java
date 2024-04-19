package com.lego.system.entity;

import com.lego.core.data.ActionType;
import com.lego.core.data.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "sys_operation_log")
public class SysOperationLog extends BaseEntity {

    private String entityCode;
    private String entityName;
    private String actionType;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    private SysEmployee operator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private SysPermission permission;

    protected SysOperationLog() {
    }

    public SysOperationLog(ActionType actionType, SysEmployee operator, SysPermission permission) {
        super(actionType.getName());
        this.actionType = actionType.getCode();
        this.operator = operator;
        this.permission = permission;
    }
}
