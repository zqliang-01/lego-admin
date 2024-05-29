package com.lego.doc.service;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.doc.dto.DocBookInfo;
import com.lego.doc.vo.DocBookCreateVO;
import com.lego.doc.vo.DocBookModifyVO;

public interface IDocBookService {

    LegoPage<DocBookInfo> findPageBy(String operatorCode, GenericSearchVO vo);

    LegoPage<DocBookInfo> findDisablePageBy(String operatorCode, GenericSearchVO vo);

    LegoPage<DocBookInfo> findPublicPageBy(String operatorCode, GenericSearchVO vo);

    DocBookInfo findBy(String operatorCode, String code);

    void update(String operatorCode, DocBookModifyVO vo);

    void add(String operatorCode, DocBookCreateVO vo);

    void disable(String operatorCode, String code);

    void enable(String operatorCode, String code);

    void delete(String operatorCode, String code);
}
