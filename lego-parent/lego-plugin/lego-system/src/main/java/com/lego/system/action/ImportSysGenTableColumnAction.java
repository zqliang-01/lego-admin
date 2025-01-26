package com.lego.system.action;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.data.mybatis.mapper.MetaTableMapper;
import com.lego.core.dto.MetaTableColumnInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.module.gen.GenConstants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.vo.SysPermissionCode;

import java.text.MessageFormat;
import java.util.List;

public class ImportSysGenTableColumnAction extends MaintainAction {

    private String tableCode;
    private List<MetaTableColumnInfo> tableColumns;

    private ISysGenTableColumnDao columnDao = getDao(ISysGenTableColumnDao.class);
    private MetaTableMapper tableMapper = LegoBeanFactory.getBean(MetaTableMapper.class);

    public ImportSysGenTableColumnAction(String operatorCode, String tableCode, List<MetaTableColumnInfo> tableColumns) {
        super(SysPermissionCode.manageGenTable, operatorCode);
        this.tableCode = tableCode;
        this.tableColumns = tableColumns;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(tableCode), "表名不能为空！");
        BusinessException.check(CollectionUtil.isNotEmpty(tableColumns), "数据表[{0}]未查询到字段信息，初始化失败！", tableCode);
    }

    @Override
    protected void doRun() {
        deleteColumns();
        for (MetaTableColumnInfo tableColumn : tableColumns) {
            if (GenConstants.COLUMNNAME_IGNORE_GEN.contains(tableColumn.getColumnName().toLowerCase())) {
                continue;
            }
            new AddSysGenTableColumnInitAction(operatorCode, tableCode, tableColumn).run();
        }
        this.description = MessageFormat.format("初始化数据表[{0}]字段", tableCode);
    }

    private void deleteColumns() {
        List<SysGenTableColumn> columns = columnDao.findBy(tableCode);
        for (SysGenTableColumn column : columns) {
            column.removeAttribute();
        }
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
