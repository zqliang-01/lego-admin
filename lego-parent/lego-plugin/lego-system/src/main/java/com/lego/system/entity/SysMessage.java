package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.system.entity.simpletype.SysMessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "sys_message")
public class SysMessage extends BaseEntity {

    private String content;
    private boolean readed;
    private Date readTime;
    private String entityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private SysCustomForm form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private SysMessageType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private SysEmployee creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private SysEmployee recipient;

    protected SysMessage() {
    }

    public SysMessage(String name, SysMessageType type, SysEmployee creator, SysEmployee recipient) {
        super(name);
        this.type = type;
        this.creator = creator;
        this.recipient = recipient;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("标题", this.getName());
        attributes.put("内容", StringUtil.toString(content));
        attributes.put("已读", readed ? "是" : "否");
        attributes.put("已读时间", DateUtil.toDateString(readTime));
        attributes.put("对象编码", StringUtil.toString(entityCode));
        attributes.put("表单", EntityUtil.toString(form));
        attributes.put("类型", EntityUtil.toString(type));
        attributes.put("创建人", EntityUtil.toString(creator));
        attributes.put("接收人", EntityUtil.toString(recipient));
    }
}
