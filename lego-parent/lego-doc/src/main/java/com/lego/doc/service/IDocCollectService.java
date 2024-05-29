package com.lego.doc.service;

import com.lego.core.dto.LegoPage;
import com.lego.doc.dto.DocCollectInfo;
import com.lego.doc.vo.DocCollectSearchVO;

public interface IDocCollectService {

    LegoPage<DocCollectInfo> findPageBy(String operatorCode, DocCollectSearchVO vo);

    String add(String operatorCode, String nodeCode);

    void delete(String operatorCode, String code);

    String findCodeBy(String operatorCode, String node);
}
