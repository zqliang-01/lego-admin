package com.lego.doc.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocNode;

import java.util.List;

public interface IDocNodeDao extends IGenericDao<DocNode> {

    List<DocNode> findBy(DocBook book);

    List<DocNode> findBy(DocBook book, String type);

    List<DocNode> findChildrenBy(DocBook book, DocNode node, String type);
}
