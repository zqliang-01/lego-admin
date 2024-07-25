package com.lego.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.system.action.AddSysColumnSortAction;
import com.lego.system.action.ModifySysColumnSortAction;
import com.lego.system.action.ModifySysColumnWidthAction;
import com.lego.system.assembler.SysColumnSortAssembler;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysEmployee;
import com.lego.system.service.ISysColumnSortService;
import com.lego.system.vo.SysColumnSortModifyVO;
import com.lego.system.vo.SysColumnWidthModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysColumnSortService extends BusService<ISysColumnSortDao, SysColumnSortAssembler> implements ISysColumnSortService {

    @Autowired
    private ISysCustomFieldDao fieldDao;

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Override
    public List<SysColumnSortInfo> findAndInitBy(String formCode, String employeeCode) {
        List<SysCustomField> fields = fieldDao.findValidBy(formCode);
        List<SysColumnSort> columnSorts = dao.findByForm(formCode, employeeCode);
        for (SysColumnSort columnSort : columnSorts) {
            Optional<SysCustomField> field = fields.stream()
                .filter(f -> f.equals(columnSort.getField()))
                .findAny();
            if (field.isPresent()) {
                fields.remove(field.get());
            }
        }
        if (CollectionUtil.isNotEmpty(fields)) {
            SysEmployee operator = employeeDao.findByCode(employeeCode);
            int maxSn = columnSorts.stream()
                .map(SysColumnSort::getSn)
                .max(Integer::compare)
                .orElse(0);
            AddSysColumnSortAction addSortAction = new AddSysColumnSortAction(operator, fields, maxSn);
            addSortAction.run();
            columnSorts.addAll(addSortAction.getSorts());
        }
        return assembler.create(columnSorts);
    }

    @Override
    public void update(String employeeCode, List<SysColumnSortModifyVO> vos) {
        new ModifySysColumnSortAction(employeeCode, vos).run();
    }

    @Override
    public void updateWidth(String loginCode, SysColumnWidthModifyVO vo) {
        new ModifySysColumnWidthAction(loginCode, vo).run();
    }

}
