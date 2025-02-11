package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.enums.FieldTypeEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.entity.SysGenTable;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.vo.SysGenTableColumnCreateVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysGenTableColumnAction extends AddAction<SysGenTableColumn, ISysGenTableColumnDao> {

    private SysGenTableColumnCreateVO vo;

    private ISysGenTableDao tableDao = getDao(ISysGenTableDao.class);
    private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

    public AddSysGenTableColumnAction(String operatorCode, SysGenTableColumnCreateVO vo) {
        super(SysPermissionCode.manageGenTable, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getTableCode()), "所属数据表不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "字段列名不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getComment()), "字段描述不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getFormType()), "表单类型不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getJavaField()), "java属性不能为空！");

        if (FieldTypeEnum.ENTITY.equals(vo.getFormType())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getRelativeTable()), "关联表不能为空！");
        }
        if (!FieldTypeEnum.ENTITY.equals(vo.getFormType())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getJavaFieldType()), "java类型不能为空！");
        }
    }

    @Override
    protected SysGenTableColumn createTargetEntity() {
        SysGenTable table = tableDao.findByCode(vo.getTableCode());
        SysGenTableColumn column = new SysGenTableColumn(vo.getName());
        column.setSn(vo.getSn());
        column.setComment(vo.getComment());
        column.setDataType(vo.getDataType());
        column.setFormType(vo.getFormType());
        column.setRequired(vo.isRequired());
        column.setUniqueness(vo.isUnique());
        column.setJavaField(vo.getJavaField());
        column.setJavaFieldType(getJavaFieldType());
        column.setCreator(employeeDao.findByCode(operatorCode));
        column.setTable(table);
        column.setAttributes(vo.getAttributes());
        return column;
    }

    private String getJavaFieldType() {
        if (FieldTypeEnum.ENTITY.equals(vo.getFormType())) {
            SysGenTable relativeTable = tableDao.findByCode(vo.getRelativeTable());
            return relativeTable.getQualifiedName();
        }
        return vo.getJavaFieldType();
    }
}
