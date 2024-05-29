package com.lego.doc.entity;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Map;

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
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("节点", EntityUtil.toString(node));
    }
}
