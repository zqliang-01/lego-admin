package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.system.action.AddSysCustomFormAction;
import com.lego.system.action.DeleteSysCustomFormAction;
import com.lego.system.action.ModifySysCustomFormAction;
import com.lego.system.action.ModifySysCustomFormFieldAction;
import com.lego.system.assembler.SysCustomFormAssembler;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.dto.SysCustomFormPermissionInfo;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import com.lego.system.entity.SysPermission;
import com.lego.system.service.ISysCustomFormService;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysCustomFormFieldModifyVO;
import com.lego.system.vo.SysCustomFormModifyVO;
import com.lego.system.vo.SysCustomFormSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysCustomFormService extends BusService<ISysCustomFormDao, SysCustomFormAssembler> implements ISysCustomFormService {

    @Autowired
    private ISysGenTableDao tableDao;

    @Autowired
    private ISysPermissionDao permissionDao;

    @Override
    public LegoPage<SysCustomFormInfo> findBy(SysCustomFormSearchVO vo) {
        LegoPage<SysCustomForm> page = dao.findBy(vo);
        return assembler.create(page);
    }

    @Override
    public SysCustomFormInfo findBy(String code) {
        SysCustomForm form = dao.findByCode(code);
        return assembler.create(form);
    }

    @Override
    public SysCustomFormPermissionInfo findPermissionBy(String code) {
        SysCustomForm form = dao.findByCode(code);
        SysPermission permission = permissionDao.findByCode(form.getTable().getPermissionCode());
        return assembler.createPermission(form, permission);
    }

    @Override
    public void modifyField(String operatorCode, SysCustomFormFieldModifyVO vo) {
        new ModifySysCustomFormFieldAction(operatorCode, vo).run();
    }

    @Override
    public void modify(String operatorCode, SysCustomFormModifyVO vo) {
        new ModifySysCustomFormAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, SysCustomFormCreateVO vo) {
        new AddSysCustomFormAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteSysCustomFormAction(operatorCode, code).run();
    }

    @Override
    public SysCustomFormInfo findInitByTable(String tableCode) {
        SysGenTable table = tableDao.findByCode(tableCode);
        SysCustomFormInfo info = new SysCustomFormInfo();
        info.setCode(table.getCode() + "_form");
        info.setName(table.getName());
        info.setTable(assembler.createTypeInfo(table));
        info.setAddApiUrl(table.createApiUrl("add"));
        info.setDeleteApiUrl(table.createApiUrl("delete"));
        info.setUpdateApiUrl(table.createApiUrl("update"));
        info.setQueryApiUrl(table.createApiUrl("list"));
        info.setDetailApiUrl(table.createApiUrl("get"));
        info.setSimpleApiUrl(table.createApiUrl("get-simple"));
        info.setExportAllApiUrl(table.createApiUrl("export-all"));
        info.setExportApiUrl(table.createApiUrl("export"));
        return info;
    }

    @Override
    public List<TypeInfo> findSimpleType() {
        List<SysCustomForm> forms = dao.findAll();
        return assembler.createTypeInfo(forms);
    }

    @Override
    public String findTableCodeBy(String code) {
        SysCustomForm form = dao.findByCode(code);
        return EntityUtil.getCode(form.getTable());
    }

}
