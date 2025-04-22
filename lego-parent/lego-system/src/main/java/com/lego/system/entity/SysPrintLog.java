package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Getter
@Entity
@Table(name = "sys_print_log")
public class SysPrintLog extends BaseEntity {

    @Setter
    private String content;
    @Setter
    private String entityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private SysPrintTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private SysPermission permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private SysEmployee creator;

    protected SysPrintLog() {
    }

    public SysPrintLog(SysPrintTemplate template, SysPermission permission, SysEmployee creator) {
        super(template.getName());
        this.permission = permission;
        this.template = template;
        this.creator = creator;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        attributes.put("内容", StringUtil.getMD5(content));
        attributes.put("模板", EntityUtil.toString(template));
        attributes.put("领域编码", StringUtil.toString(entityCode));
        attributes.put("操作功能", EntityUtil.toString(permission));
    }

}
