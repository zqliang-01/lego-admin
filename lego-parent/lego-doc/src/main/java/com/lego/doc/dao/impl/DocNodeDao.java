package com.lego.doc.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocNodeDao;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocNode;

import java.util.List;

public class DocNodeDao extends GenericDao<DocNode> implements IDocNodeDao {

    @Override
    public List<DocNode> findBy(DocBook book) {
        return findBy(book, null);
    }

    @Override
    public List<DocNode> findBy(DocBook book, String type) {
        QueryHandler<DocNode> query = createQueryHandler();
        query.condition("t.book = :book").param("book", book);
        query.condition("t.enable = :enable").param("enable", true);
        if (StringUtil.isNotBlank(type)) {
            query.condition("t.type.code = :typeCode").param("typeCode", type);
        }
        query.order("t.sn, t.createTime");
        return query.findList();
    }

    @Override
    public List<DocNode> findChildrenBy(DocBook book, DocNode node, String type) {
        QueryHandler<DocNode> query = createQueryHandler();
        query.condition("t.book = :book").param("book", book);
        query.condition("t.enable = :enable").param("enable", true);
        query.condition("t.parent = :node").param("node", node);
        if (StringUtil.isNotBlank(type)) {
            query.condition("t.type.code = :typeCode").param("typeCode", type);
        }
        query.order("t.sn, t.createTime");
        return query.findList();
    }
}
