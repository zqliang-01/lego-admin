package com.lego.doc.action;

import com.lego.core.action.AddAction;
import com.lego.core.dto.FileUploadInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.core.web.upload.FileHandler;
import com.lego.doc.dao.IDocFileDao;
import com.lego.doc.entity.DocFile;
import com.lego.doc.entity.simpletype.DocFileLocation;
import com.lego.doc.entity.simpletype.DocFileType;
import com.lego.doc.vo.DocFileTypeCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class AddDocFileAction extends AddAction<DocFile, IDocFileDao> {

    private String fileName;
    private MultipartFile springFile;
    private FileHandler fileHandler = LegoBeanFactory.getBean(FileHandler.class);
    private static final List<String> imageSuffixes = Arrays.asList(".gif", ".png", ".jpg", ".jpeg");

    public AddDocFileAction(String operatorCode, MultipartFile springFile) {
        super("doc_book", operatorCode);
        this.springFile = springFile;
        this.fileName = springFile.getOriginalFilename();
    }

    @Override
    protected void preprocess() {
        BusinessException.check(springFile != null && !springFile.isEmpty(), "上传的文件内容为空，请选择上传的文件！");
    }

    @Override
    protected DocFile createTargetEntity() {
        DocFile file = new DocFile(fileName);
        file.setSize(springFile.getSize());
        file.setType(getFileType());

        FileUploadInfo uploadInfo = uploadFile();
        file.setPath(uploadInfo.getPath());
        file.setCreatorCode(operatorCode);
        file.setLocation(findByCode(DocFileLocation.class, uploadInfo.getLocationCode()));
        return file;
    }

    private FileUploadInfo uploadFile() {
        try {
            return fileHandler.upload(springFile.getInputStream(), "doc", fileName);
        } catch (Exception e) {
            throw new CoreException("附件上传失败", e);
        }
    }

    private DocFileType getFileType() {
        BusinessException.check(StringUtil.isNotBlank(fileName), "文件名称不能为空！");
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (imageSuffixes.contains(suffix)) {
            return findByCode(DocFileType.class, DocFileTypeCode.IMAGE);
        }
        return findByCode(DocFileType.class, DocFileTypeCode.FILE);
    }

    public String getFileCode() {
        return this.targetEntity.getCode();
    }
}
