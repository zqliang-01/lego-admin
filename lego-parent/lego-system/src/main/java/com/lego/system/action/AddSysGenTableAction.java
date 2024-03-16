package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysGenTableCreateVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysGenTableAction extends AddAction<SysGenTable, ISysGenTableDao> {

    private SysGenTableCreateVO vo;

    private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

    public AddSysGenTableAction(String operatorCode, SysGenTableCreateVO vo) {
        super(SysPermissionCode.manageGenTable, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "表名不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "功能名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getPackageName()), "包名不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getAppCode()), "模块编码不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getClassName()), "类名不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getFieldName()), "属性名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getUrlName()), "资源名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getPermissionCode()), "权限编码不能为空！");

        SysGenTable table = entityDao.findByUnsureCode(vo.getCode());
        BusinessException.check(table == null, "数据表[{0}]已经创建，请勿重复创建！", vo.getCode());
    }

    @Override
    protected SysGenTable createTargetEntity() {
        SysGenTable table = new SysGenTable(vo.getCode().toLowerCase(), vo.getName());
        table.setPath(vo.getPath());
        table.setComment(vo.getComment());
        table.setAppCode(vo.getAppCode());
        table.setUrlName(vo.getUrlName());
        table.setClassName(vo.getClassName());
        table.setFieldName(vo.getFieldName());
        table.setPackageName(vo.getPackageName());
        table.setPermissionCode(vo.getPermissionCode());
        table.setCreator(employeeDao.findByCode(operatorCode));
        return table;
    }

    @Override
    protected void postprocess() {
        new ImportSysGenTableColumnAction(operatorCode, vo.getCode()).run();
    }
}
