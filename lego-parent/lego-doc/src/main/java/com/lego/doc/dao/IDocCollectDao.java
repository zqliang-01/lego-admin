package com.lego.doc.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.doc.entity.DocCollect;
import com.lego.doc.entity.DocNode;
import com.lego.doc.vo.DocCollectSearchVO;

import java.util.List;

public interface IDocCollectDao extends IGenericDao<DocCollect> {

    List<DocCollect> findBy(List<DocNode> nodes);

    List<DocCollect> findBy(String nodeCode, String creatorCode);

    LegoPage<DocCollect> findBy(String creatorCode, DocCollectSearchVO vo);
}
