package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.vo.SysColumnSortModifyVO;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModifySysColumnSortAction extends MaintainAction {

    private List<SysColumnSortModifyVO> vos;

    private ISysColumnSortDao sortDao = getDao(ISysColumnSortDao.class);

    public ModifySysColumnSortAction(String operatorCode, List<SysColumnSortModifyVO> vos) {
        super(SysPermissionCode.manage, operatorCode);
        this.vos = vos;
    }

    @Override
    protected void doRun() {
        int sn = 0;
        List<String> codes = vos.stream().map(SysColumnSortModifyVO::getCode).collect(Collectors.toList());
        List<SysColumnSort> sorts = sortDao.findByCodes(codes);
        for (SysColumnSortModifyVO vo : vos) {
            Optional<SysColumnSort> result = sorts.stream()
                .filter(s -> s.getCode().equals(vo.getCode()))
                .findAny();
            if (result.isPresent()) {
                SysColumnSort sort = result.get();
                sort.setVisible(vo.isVisible());
                sort.setSn(sn++);
            }
        }
        sortDao.saveAll(sorts);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "更新菜单数据表字段排序";
    }
}
