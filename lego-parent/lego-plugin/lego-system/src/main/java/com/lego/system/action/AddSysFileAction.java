package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.dto.FileUploadInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.core.web.upload.FileHandler;
import com.lego.system.dao.ISysFileDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysFile;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.simpletype.SysFileLocation;
import com.lego.system.entity.simpletype.SysFileType;
import com.lego.system.vo.SysFileTypeCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class AddSysFileAction extends AddAction<SysFile, ISysFileDao> {

    private String fileName;
    private String entityCode;
    private MultipartFile springFile;

    private FileHandler fileHandler = LegoBeanFactory.getBean(FileHandler.class);
    private static final List<String> imageSuffixes = Arrays.asList(".gif", ".png", ".jpg", ".jpeg");

    public AddSysFileAction(String operatorCode, MultipartFile springFile, String entityCode, String permissionCode) {
        super(permissionCode, operatorCode);
        this.springFile = springFile;
        this.entityCode = entityCode;
        this.fileName = springFile.getOriginalFilename();
    }

    @Override
    protected void preprocess() {
        BusinessException.check(springFile != null && !springFile.isEmpty(), "上传的文件内容为空，请选择上传的文件！");
        BusinessException.check(StringUtil.isNotBlank(entityCode), "附件归属领域信息为空！");
    }

    @Override
    protected SysFile createTargetEntity() {
        SysFile file = new SysFile(fileName, entityCode);
        file.setEntityCode(entityCode);
        file.setSize(springFile.getSize());
        file.setType(getFileType());

        FileUploadInfo uploadInfo = uploadFile();
        file.setPath(uploadInfo.getPath());
        file.setCreator(findByCode(SysEmployee.class, operatorCode));
        file.setLocation(findByCode(SysFileLocation.class, uploadInfo.getLocationCode()));
        file.setPermission(findByCode(SysPermission.class, permissionCode));
        return file;
    }

    private FileUploadInfo uploadFile() {
        try {
            return fileHandler.upload(springFile.getInputStream(), "sys", fileName);
        } catch (Exception e) {
            throw new CoreException("附件上传失败", e);
        }
    }

    private SysFileType getFileType() {
        BusinessException.check(StringUtil.isNotBlank(fileName), "附件名称不能为空！");
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (imageSuffixes.contains(suffix)) {
            return findByCode(SysFileType.class, SysFileTypeCode.IMAGE);
        }
        return findByCode(SysFileType.class, SysFileTypeCode.FILE);
    }

    public String getFileCode() {
        return this.targetEntity.getCode();
    }
}
