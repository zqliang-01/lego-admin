package com.lego.system.action;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson2.JSON;
import com.lego.core.action.MaintainAction;
import com.lego.core.common.Constants;
import com.lego.core.data.DataSourceConfig;
import com.lego.core.enums.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.util.SqlRunnerUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.dto.SysAppPackageInfo;
import com.lego.system.util.PomUtil;
import com.lego.system.vo.SysPermissionCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ImportAppPackageAction extends MaintainAction {

    private String rootPath;
    private MultipartFile springFile;

    private DataSourceConfig dataSourceConfig = LegoBeanFactory.getBean(DataSourceConfig.class);

    public ImportAppPackageAction(String operatorCode, MultipartFile springFile, String rootPath) {
        super(SysPermissionCode.manageSystem, operatorCode);
        this.springFile = springFile;
        this.rootPath = rootPath;
    }

    @Override
    protected void preprocess() {
        String fileName = springFile.getOriginalFilename();
        BusinessException.check(StringUtil.isNotBlank(fileName), "文件名称不能为空！");
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        BusinessException.check("zip".equals(fileType), "只支持zip类型的程序包导入，模块导入失败！");
        BusinessException.check(StringUtil.isNotBlank(rootPath), "项目源码路径未配置，模块导入失败！");
    }

    @Override
    protected void doRun() {
        File packageFile = unzipPackageFile();
        SysAppPackageInfo info = getMetaInfo(packageFile, "META-INFO");
        for (Map.Entry<String, String> entry : info.getPath().entrySet()) {
            String path = packageFile.getAbsoluteFile() + entry.getKey();
            String targetPath = rootPath + entry.getValue();
            FileUtil.copy(path, targetPath, true);
        }
        updateRootPom(info);
        updateAdminPom(info);
        execSql(packageFile);
    }

    private void updateRootPom(SysAppPackageInfo info) {
        String pomPath = rootPath + File.separator + "lego-parent" + File.separator + "pom.xml";
        Model model = PomUtil.getModel(pomPath);
        Optional<String> exists = model.getModules().stream().filter(m -> m.equals(info.getCode())).findAny();
        if (!exists.isPresent()) {
            model.addModule(info.getCode());
            PomUtil.saveModel(model, pomPath);
        }
    }

    private void updateAdminPom(SysAppPackageInfo info) {
        String pomPath = rootPath + File.separator + "lego-parent" + File.separator + "lego-admin" + File.separator + "pom.xml";
        Model model = PomUtil.getModel(pomPath);
        Optional<Dependency> exists = model.getDependencies().stream().filter(d -> d.getArtifactId().equals(info.getCode())).findAny();
        if (!exists.isPresent()) {
            Dependency dependency = new Dependency();
            dependency.setGroupId("com.lego");
            dependency.setArtifactId(info.getCode());
            dependency.setVersion(info.getVersion());
            model.addDependency(dependency);
            PomUtil.saveModel(model, pomPath);
        } else {
            Dependency dependency = exists.get();
            dependency.setVersion(info.getVersion());
            PomUtil.saveModel(model, pomPath);
        }
    }

    @SneakyThrows
    private void execSql(File packageFile) {
        File sqlFile = getChildrenFile(packageFile, "sql");
        if (sqlFile == null) {
            return;
        }
        List<File> sqlFiles = FileUtil.loopFiles(sqlFile);
        try (Connection connection = dataSourceConfig.getDataSource().getConnection()) {
            SqlRunnerUtil.runBatchSql(sqlFiles, connection, false);
        } catch (Exception e) {
            throw new CoreException("程序包SQL脚本执行失败", e);
        }
    }

    private File getChildrenFile(File rootFile, String name) {
        for (File file : rootFile.listFiles()) {
            if (name.equals(file.getName())) {
                return file;
            }
        }
        return null;
    }

    private SysAppPackageInfo getMetaInfo(File rootFile, String name) {
        for (File file : rootFile.listFiles()) {
            if (name.equals(file.getName())) {
                String infoJson = FileUtil.readString(file, Constants.DEFAULT_CHARSET);
                return JSON.parseObject(infoJson, SysAppPackageInfo.class);
            }
        }
        throw new BusinessException("模块包缺失必要文件META-INFO，模块导入失败！");
    }

    private File unzipPackageFile() {
        try (InputStream inputStream = springFile.getInputStream()) {
            String tmpPath = FileUtil.getTmpDirPath() + "AppPackage";
            FileUtil.del(tmpPath);
            File targetFile = FileUtil.mkdir(tmpPath);
            return ZipUtil.unzip(inputStream, targetFile, Constants.DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new CoreException("读取SQL更新脚本径失败！", e);
        }
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    @Override
    protected String getEntityName() {
        return springFile.getOriginalFilename() + "模块导入";
    }
}
