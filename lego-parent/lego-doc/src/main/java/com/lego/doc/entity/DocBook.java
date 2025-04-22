package com.lego.doc.entity;

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
import java.util.Date;

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
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("是否公开", open ? "是" : "否");
        attributes.put("封面", EntityUtil.toString(cover));
    }
}
