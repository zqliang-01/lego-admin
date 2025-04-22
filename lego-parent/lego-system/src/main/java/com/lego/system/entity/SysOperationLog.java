package com.lego.system.entity;

import com.lego.core.enums.ActionType;
import com.lego.core.data.hibernate.entity.BaseEntity;
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
