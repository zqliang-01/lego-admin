package com.lego.doc.service;

import com.lego.doc.dto.DocPageInfo;
import com.lego.doc.vo.DocPageModifyVO;

public interface IDocPageService {

    DocPageInfo findBy(String operatorCode, String code);

    void update(String operatorCode, DocPageModifyVO vo);

}
