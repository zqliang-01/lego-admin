package com.lego.doc.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.web.upload.FileHandler;
import com.lego.doc.action.AddDocFileAction;
import com.lego.doc.assembler.DocFileAssembler;
import com.lego.doc.dao.IDocFileDao;
import com.lego.doc.dto.DocFileInfo;
import com.lego.doc.entity.DocFile;
import com.lego.doc.service.IDocFileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class DocFileService extends BaseService<IDocFileDao, DocFileAssembler> implements IDocFileService {

    @Autowired
    private FileHandler fileHandler;

    @Override
    public String upload(String operatorCode, MultipartFile file) {
        AddDocFileAction addAction = new AddDocFileAction(operatorCode, file);
        addAction.run();
        return addAction.getFileCode();
    }

    @Override
    public LegoPage<DocFileInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<DocFile> files = dao.findPageBy(buildCondition(vo));
        return assembler.create(files);
    }

    @Override
    public DocFileInfo findBy(String code) {
        DocFile file = dao.findByCode(code);
        return assembler.create(file);
    }

    @Override
    public TypeInfo findSimpleBy(String code) {
        DocFile file = dao.findByCode(code);
        return assembler.createTypeInfo(file);
    }

    @Override
    public void add(String operatorCode, MultipartFile springFile) {
        new AddDocFileAction(operatorCode, springFile).run();
    }

    @Override
    public void download(HttpServletResponse response, String code) {
        DocFile file = dao.findByCode(code);
        InputStream inputStream = fileHandler.download(file.getPath());
        final String contentType = ObjectUtil.defaultIfNull(FileUtil.getMimeType(file.getName()), "application/octet-stream");
        JakartaServletUtil.write(response, inputStream, contentType, file.getName());
    }

}
