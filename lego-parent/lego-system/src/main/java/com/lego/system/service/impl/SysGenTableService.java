package com.lego.system.service.impl;

import com.lego.core.common.GenConstants;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.system.action.AddSysGenTableAction;
import com.lego.system.action.ImportSysGenTableColumnAction;
import com.lego.system.action.ModifySysGenTableAction;
import com.lego.system.assembler.SysGenTableAssembler;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.entity.SysGenTable;
import com.lego.system.mapper.SysTableMapper;
import com.lego.system.service.ISysGenTableService;
import com.lego.system.vo.SysGenTableCreateVO;
import com.lego.system.vo.SysGenTableModifyVO;
import com.lego.system.vo.SysGenTableSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysGenTableService extends BusService<ISysGenTableDao, SysGenTableAssembler> implements ISysGenTableService {

    @Autowired
    private SysTableMapper tableMapper;

    @Override
    public LegoPage<SysGenTableInfo> findPageBy(SysGenTableSearchVO vo) {
        GenericConditionVO conditionVO = GenericConditionVO.create(vo);
        if (StringUtil.isNotBlank(vo.getCode())) {
            conditionVO.addItem(GenericConditionItemVO.createEqual("code", vo.getCode()));
        }
        LegoPage<SysGenTable> page = dao.findPageBy(conditionVO);
        return assembler.create(page);
    }

    @Override
    public List<TypeInfo> findNotExists() {
        List<SysGenTable> tables = dao.findNotExists();
        return assembler.createTypeInfo(tables);
    }

    @Override
    public List<TypeInfo> findAll() {
        List<SysGenTable> tables = dao.findAll();
        return assembler.createTypeInfo(tables);
    }

    @Override
    public List<TypeInfo> findSimpleType() {
        return assembler.createTypeInfo(dao.findAll());
    }

    @Override
    public SysGenTableInfo findByCode(String code) {
        SysGenTable table = dao.findByCode(code);
        return assembler.create(table);
    }

    @Override
    public List<TypeInfo> findTableName() {
        return tableMapper.selectByDBName(null);
    }

    @Override
    public SysGenTableInfo findInitBy(String code) {
        BusinessException.check(StringUtil.isNotBlank(code), "数据表名不能为空！");
        String tableName = code.toLowerCase();
        String appCode = StringUtil.substringBefore(tableName, "_");
        String fieldName = StringUtil.substringAfter(tableName, "_");

        SysGenTableInfo info = new SysGenTableInfo();
        info.setCode(tableName);
        info.setAppCode(appCode);
        info.setUrlName(fieldName.replace("_", "-"));
        info.setName(tableMapper.selectCommentByName(code));
        info.setClassName(StringUtil.toCamelCase(tableName, true));
        info.setFieldName(StringUtil.toCamelCase(fieldName, false));
        info.setPermissionCode(appCode + "_" + info.getFieldName());
        info.setPackageName(GenConstants.ROOT_PACKAGE_NAME + "." + appCode);
        return info;
    }

    @Override
    public void add(String operatorCode, SysGenTableCreateVO vo) {
        new AddSysGenTableAction(operatorCode, vo).run();
    }

    @Override
    public void modify(String operatorCode, SysGenTableModifyVO vo) {
        new ModifySysGenTableAction(operatorCode, vo).run();
    }

    @Override
    public void sync(String operatorCode, String code) {
        new ImportSysGenTableColumnAction(operatorCode, code).run();
    }

}
