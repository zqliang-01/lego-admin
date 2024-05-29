package com.lego.doc.entity;

import com.lego.core.util.EntityUtil;
import com.lego.doc.entity.simpletype.DocNodeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Map;

@Entity
@Getter
@Setter
@DiscriminatorValue("DocNodePage")
public class DocNodePage extends DocNode {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private DocPage page;

    protected DocNodePage() {
    }

    public DocNodePage(String creatorCode, DocPage page, DocBook book, DocNodeType type) {
        super(page.getName(), creatorCode, book, type);
        this.page = page;
    }

    @Override
    public boolean checkPermission(String operator) {
        return page.checkPermission(operator);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        super.doBuildReadableSnapshot(attributes);
        attributes.put("内容", EntityUtil.toString(page));
    }
}
