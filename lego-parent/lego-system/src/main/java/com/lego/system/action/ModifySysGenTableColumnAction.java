package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.entity.SysGenTable;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.vo.SysGenTableColumnModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysGenTableColumnAction extends ModifyAction<SysGenTableColumn, ISysGenTableColumnDao> {

    private SysGenTableColumnModifyVO vo;

    private ISysGenTableDao tableDao = getDao(ISysGenTableDao.class);

    public ModifySysGenTableColumnAction(String operatorCode, SysGenTableColumnModifyVO vo) {
        super(SysPermissionCode.manageGenTable, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        this.setCheckDiff(false);
        BusinessException.check(StringUtil.isNotBlank(vo.getFormType()), "表单类型不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getJavaField()), "JAVA字段名不能为空！");
        BusinessException.check(StringUtil.isNotBlank(getJavaFieldType()), "JAVA字段类型不能为空！");
        if (CustomFieldTypeEnum.ENTITY.getCode().equals(vo.getFormType())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getRelativeTableCode()), "关联表类型需选择所关联的数据表信息！");
        }
    }

    @Override
    protected void doModify(SysGenTableColumn entity) {
        entity.setSn(vo.getSn());
        entity.setComment(vo.getComment());
        entity.setFormType(vo.getFormType());
        entity.setJavaField(vo.getJavaField());
        entity.setJavaFieldType(getJavaFieldType());
        entity.setRequired(vo.isRequired());
        entity.setUniqueness(vo.isUnique());
        entity.setRelativeTable(tableDao.findByUnsureCode(vo.getRelativeTableCode()));
    }

    private String getJavaFieldType() {
        if (CustomFieldTypeEnum.ENTITY.getCode().equals(vo.getFormType())) {
            SysGenTable relativeTable = tableDao.findByCode(vo.getRelativeTableCode());
            return relativeTable.getQualifiedName();
        }
        return vo.getJavaFieldType();
    }

}
