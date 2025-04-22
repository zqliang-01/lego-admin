package com.lego.doc.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Getter
@Entity
@Table(name = "doc_collect")
public class DocCollect extends BaseEntity {

    private boolean enable;
    private String creatorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private DocNode node;

    protected DocCollect() {
    }

    public DocCollect(String creatorCode, DocNode node) {
        super(node.getName());
        this.enable = true;
        this.node = node;
        this.creatorCode = creatorCode;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("节点", EntityUtil.toString(node));
    }
}
