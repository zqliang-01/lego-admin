package com.lego.doc.service;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.doc.dto.DocFileInfo;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

public interface IDocFileService {

    String upload(String operatorCode, MultipartFile file);

    LegoPage<DocFileInfo> findPageBy(GenericSearchVO vo);

    DocFileInfo findBy(String code);

    TypeInfo findSimpleBy(String code);

    void add(String operatorCode, MultipartFile springFile);

    void download(HttpServletResponse response, String code);
}
