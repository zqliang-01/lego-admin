package com.lego.system.entity;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "sys_notice")
public class SysNotice extends BaseEntity {

    private String content;
    private boolean readed;
    private Date readTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private SysNoticeTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private SysEmployee creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private SysEmployee recipient;

    protected SysNotice() {
    }

    public SysNotice(SysNoticeTemplate template, SysEmployee creator, SysEmployee recipient) {
        super(template.getName());
        this.template = template;
        this.content = template.getContent();
        this.creator = creator;
        this.recipient = recipient;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("标题", this.getName());
        attributes.put("内容", StringUtil.toString(content));
        attributes.put("模板", EntityUtil.toString(template));
        attributes.put("已读", readed ? "是" : "否");
        attributes.put("已读时间", DateUtil.toDateString(readTime));
        attributes.put("创建人", EntityUtil.toString(creator));
        attributes.put("接收人", EntityUtil.toString(recipient));
    }
}
