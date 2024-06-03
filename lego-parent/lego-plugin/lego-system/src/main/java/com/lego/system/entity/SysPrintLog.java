package com.lego.system.entity;

import com.lego.core.data.hibernate.BaseEntity;
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
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        attributes.put("内容", StringUtil.getMD5(content));
        attributes.put("模板", EntityUtil.toString(template));
        attributes.put("领域编码", StringUtil.toString(entityCode));
        attributes.put("操作功能", EntityUtil.toString(permission));
    }

}
