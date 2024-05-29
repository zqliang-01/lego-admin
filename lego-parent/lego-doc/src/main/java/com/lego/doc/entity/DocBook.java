package com.lego.doc.entity;

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
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "doc_book")
public class DocBook extends BaseEntity {

    private boolean open;
    private boolean enable;
    private String creatorCode;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_id")
    private DocFile cover;

    private Date updateTime;
    private int sn;

    protected DocBook() {
    }

    public DocBook(String name, String creatorCode) {
        super(name);
        this.enable = true;
        this.creatorCode = creatorCode;
    }

    public boolean checkPermission(String operator) {
        return StringUtil.equals(creatorCode, operator);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("是否公开", open ? "是" : "否");
        attributes.put("封面", EntityUtil.toString(cover));
    }
}
