package com.lego.doc.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocCollectDao;
import com.lego.doc.entity.DocCollect;
import com.lego.doc.entity.DocNode;
import com.lego.doc.vo.DocCollectSearchVO;

import java.util.List;

public class DocCollectDao extends GenericDao<DocCollect> implements IDocCollectDao {

    @Override
    public List<DocCollect> findBy(List<DocNode> nodes) {
        QueryHandler<DocCollect> query = createQueryHandler();
        query.condition("t.node IN (:nodes)").param("nodes", nodes);
        query.condition("t.node.enable = :nodeEnable").param("nodeEnable", true);
        query.condition("t.node.book.enable = :bookEnable").param("bookEnable", true);
        return query.findList();
    }

    @Override
    public List<DocCollect> findBy(String nodeCode, String creatorCode) {
        QueryHandler<DocCollect> query = createQueryHandler();
        query.condition("t.node.code = :nodeCode").param("nodeCode", nodeCode);
        query.condition("t.node.enable = :nodeEnable").param("nodeEnable", true);
        query.condition("t.node.book.enable = :bookEnable").param("bookEnable", true);
        query.condition("t.creatorCode = :creatorCode").param("creatorCode", creatorCode);
        return query.findList();
    }

    @Override
    public LegoPage<DocCollect> findBy(String creatorCode, DocCollectSearchVO vo) {
        QueryHandler<DocCollect> query = createQueryHandler();
        query.condition("t.node.enable = :nodeEnable").param("nodeEnable", true);
        query.condition("t.node.book.enable = :bookEnable").param("bookEnable", true);
        query.condition("t.creatorCode = :creatorCode").param("creatorCode", creatorCode);
        if (StringUtil.isNotBlank(vo.getName())) {
            query.condition("t.node.name LIKE :name").param("name", "%" + vo.getName() + "%");
        }
        return query.findPage(vo);
    }
}
