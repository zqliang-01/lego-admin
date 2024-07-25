package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysPermissionCode;

import java.util.ArrayList;
import java.util.List;

public class AddSysColumnSortAction extends MaintainAction {

    private int startSn;
    private SysEmployee operator;
    private List<SysCustomField> fields;
    private List<SysColumnSort> sorts = new ArrayList<>();

    private ISysColumnSortDao sortDao = getDao(ISysColumnSortDao.class);

    public AddSysColumnSortAction(SysEmployee operator, List<SysCustomField> fields, int startSn) {
        super(SysPermissionCode.manage, operator.getCode());
        this.fields = fields;
        this.startSn = startSn;
        this.operator = operator;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(fields != null, "表格排序创建失败，表单字段不能为空！");
    }

    @Override
    protected void doRun() {
        for (SysCustomField field : fields) {
            SysColumnSort columnSort = new SysColumnSort(field, operator);
            columnSort.setSn(++startSn);
            sorts.add(columnSort);
        }
        sortDao.saveAll(sorts);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    @Override
    protected String getEntityName() {
        return "创建排序";
    }

    public List<SysColumnSort> getSorts() {
        return sorts;
    }
}
