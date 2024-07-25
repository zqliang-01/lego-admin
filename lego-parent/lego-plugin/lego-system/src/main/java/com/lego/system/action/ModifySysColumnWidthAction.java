package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysColumnWidthModifyVO;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;
import java.util.Optional;

public class ModifySysColumnWidthAction extends MaintainAction {

    private SysColumnWidthModifyVO vo;

    private ISysColumnSortDao sortDao = getDao(ISysColumnSortDao.class);
    private ISysCustomFieldDao fieldDao = getDao(ISysCustomFieldDao.class);

    public ModifySysColumnWidthAction(String operatorCode, SysColumnWidthModifyVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void doRun() {
        SysColumnSort columnSort = sortDao.findByField(vo.getFieldCode(), operatorCode);
        if (columnSort == null) {
            initColumnSort();
            return;
        }
        columnSort.setWidth(vo.getWidth());
        sortDao.save(columnSort);
    }

    private void initColumnSort() {
        SysCustomField customField = findByCode(SysCustomField.class, vo.getFieldCode());
        String formCode = customField.getForm().getCode();
        List<SysCustomField> fields = fieldDao.findValidBy(formCode);
        List<SysColumnSort> columnSorts = sortDao.findByForm(formCode, operatorCode);
        for (SysColumnSort sort : columnSorts) {
            Optional<SysCustomField> field = fields.stream()
                .filter(f -> f.equals(sort.getField()))
                .findAny();
            if (field.isPresent()) {
                fields.remove(field.get());
            }
        }
        SysEmployee employee = findByCode(SysEmployee.class, operatorCode);
        int maxSn = columnSorts.stream().map(SysColumnSort::getSn).max(Integer::compare).orElse(0);
        AddSysColumnSortAction addSortAction = new AddSysColumnSortAction(employee, fields, maxSn);
        addSortAction.run();
        for (SysColumnSort sort : addSortAction.getSorts()) {
            if (sort.getField().equals(customField)) {
                sort.setWidth(vo.getWidth());
                sortDao.save(sort);
                break;
            }
        }
    }

    @Override
    protected void createLog() {
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "修改列宽";
    }
}
