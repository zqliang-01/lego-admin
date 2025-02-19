package com.lego.system.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.util.StringUtil;
import com.lego.core.web.upload.FileHandler;
import com.lego.system.action.AddSysFileAction;
import com.lego.system.action.DeleteSysFileAction;
import com.lego.system.action.ModifySysFileAction;
import com.lego.system.assembler.SysFileAssembler;
import com.lego.system.dao.ISysFileDao;
import com.lego.system.dto.SysFileInfo;
import com.lego.system.entity.SysFile;
import com.lego.system.service.ISysFileService;
import com.lego.system.vo.SysPermissionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

@Service
public class SysFileService extends BaseService<ISysFileDao, SysFileAssembler> implements ISysFileService {

    @Autowired
    private FileHandler fileHandler;

    @Override
    public SysFileInfo findBy(String code) {
        SysFile file = dao.findByCode(code);
        return assembler.create(file);
    }

    @Override
    public List<SysFileInfo> findBy(String entityCode, String permissionCode) {
        List<SysFile> files = dao.findBy(entityCode, permissionCode);
        return assembler.create(files);
    }

    @Override
    public String upload(String operatorCode, MultipartFile file, String entityCode, String permissionCode) {
        AddSysFileAction addFileAction = new AddSysFileAction(operatorCode, file, entityCode, permissionCode);
        addFileAction.run();
        return addFileAction.getFileCode();
    }

    @Override
    public void delete(String operatorCode, String permissionCode, String code) {
        permissionCode = StringUtil.isBlank(permissionCode) ? SysPermissionCode.manage : permissionCode;
        new DeleteSysFileAction(permissionCode, operatorCode, code).run();
    }

    @Override
    public void download(HttpServletResponse response, String code) {
        SysFile file = dao.findByCode(code);
        InputStream inputStream = fileHandler.download(file.getPath());
        final String contentType = ObjectUtil.defaultIfNull(FileUtil.getMimeType(file.getName()), "application/octet-stream");
        if (file.isImage()) {
            ServletUtil.write(response, inputStream, contentType);
            return;
        }
        ServletUtil.write(response, inputStream, contentType, file.getName());
    }

    @Override
    public void modify(String loginCode, String permissionCode, String code, String name) {
        new ModifySysFileAction(permissionCode, loginCode, code, name).run();
    }

}
