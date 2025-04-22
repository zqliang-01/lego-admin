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

@Setter
@Getter
@Entity
@Table(name = "sys_print_template")
public class SysPrintTemplate extends BaseEntity {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private SysCustomForm form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private SysEmployee creator;

    protected SysPrintTemplate() {
    }

    public SysPrintTemplate(String name, SysCustomForm form, SysEmployee creator) {
        super(name);
        this.form = form;
        this.creator = creator;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        String md5Str = "";
        if (StringUtil.isNotBlank(content)) {
            md5Str = StringUtil.getMD5(content);
        }
        attributes.put("内容", md5Str);
        attributes.put("表单", EntityUtil.toString(form));
    }

}
