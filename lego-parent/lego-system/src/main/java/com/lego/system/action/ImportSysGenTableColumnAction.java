package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.common.GenConstants;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dto.SysDBTableColumnInfo;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.mapper.SysTableMapper;
import com.lego.system.vo.SysPermissionCode;

import java.text.MessageFormat;
import java.util.List;

public class ImportSysGenTableColumnAction extends MaintainAction {

    private String tableCode;

    private ISysGenTableColumnDao columnDao = getDao(ISysGenTableColumnDao.class);
    private SysTableMapper tableMapper = LegoBeanFactory.getBean(SysTableMapper.class);

    public ImportSysGenTableColumnAction(String operatorCode, String tableCode) {
        super(SysPermissionCode.manageGenTable, operatorCode);
        this.tableCode = tableCode;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(tableCode), "表名不能为空！");
    }

    @Override
    protected void doRun() {
        deleteColumns();
        List<SysDBTableColumnInfo> tableColumns = tableMapper.selectColumns(tableCode);
        for (SysDBTableColumnInfo tableColumn : tableColumns) {
            if (GenConstants.COLUMNNAME_IGNORE_GEN.contains(tableColumn.getColumnName().toLowerCase())) {
                continue;
            }
            new AddSysGenTableColumnAction(operatorCode, tableCode, tableColumn).run();
        }
        this.description = MessageFormat.format("初始化数据表[{0}]字段", tableCode);
    }

    private void deleteColumns() {
        List<SysGenTableColumn> columns = columnDao.findBy(tableCode);
        columnDao.deleteAllInBatch(columns);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return MessageFormat.format("数据表[{0}]", tableCode);
    }
}
