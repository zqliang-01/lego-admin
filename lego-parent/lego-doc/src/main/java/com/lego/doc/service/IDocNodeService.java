package com.lego.doc.service;

import com.lego.doc.dto.DocNodeDetailInfo;
import com.lego.doc.dto.DocNodeInfo;
import com.lego.doc.vo.DocNodeCreateVO;
import com.lego.doc.vo.DocNodeModifyVO;

import java.util.List;

public interface IDocNodeService {

    void update(String operatorCode, DocNodeModifyVO vo);

    void add(String operatorCode, DocNodeCreateVO vo);

    List<DocNodeInfo> findBy(String bookCode, String type, String operatorCode);

    List<DocNodeDetailInfo> findChildrenBy(String code, String type, String operatorCode);

    void disable(String loginCode, String code);

    DocNodeDetailInfo findBy(String operatorCode, String code);
}
