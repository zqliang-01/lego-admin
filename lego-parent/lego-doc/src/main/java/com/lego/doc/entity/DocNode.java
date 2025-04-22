package com.lego.doc.entity;

import com.lego.core.data.hibernate.entity.TreeEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.doc.entity.simpletype.DocNodeType;
import com.lego.doc.vo.DocNodeTypeCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "doc_node")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "class_type", discriminatorType = DiscriminatorType.STRING)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class DocNode extends TreeEntity<DocNode> {

    private boolean enable;
    private String creatorCode;
    private String description;
    private Date updateTime;
    private int sn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private DocBook book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private DocNodeType type;

    protected DocNode() {
    }

    protected DocNode(String name, String creatorCode, DocBook book, DocNodeType type) {
        super(name);
        this.enable = true;
        this.book = book;
        this.type = type;
        this.creatorCode = creatorCode;
    }

    public boolean checkPermission(String operator) {
        return book.checkPermission(operator);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("归属图书", EntityUtil.toString(book));
        attributes.put("节点类型", EntityUtil.toString(type));
        attributes.put("创建人", StringUtil.toString(creatorCode));
        attributes.put("序号", StringUtil.toString(sn));
    }

    public boolean isFolder() {
        return DocNodeTypeCode.FOLDER.equals(type.getCode());
    }
}
