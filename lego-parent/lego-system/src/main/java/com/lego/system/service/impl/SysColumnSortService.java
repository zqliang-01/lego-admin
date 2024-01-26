package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.util.StringUtil;
import com.lego.system.action.AddSysColumnSortAction;
import com.lego.system.action.ModifySysColumnSortAction;
import com.lego.system.assembler.SysColumnSortAssembler;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.service.ISysColumnSortService;
import com.lego.system.vo.SysColumnSortModifyVO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SysColumnSortService extends BusService<ISysColumnSortDao, SysColumnSortAssembler> implements ISysColumnSortService {

    @Override
    public List<SysColumnSortInfo> findBy(String formCode, String employeeCode) {
        List<SysColumnSort> tableColumnSorts = dao.findByForm(formCode, employeeCode);
        return assembler.create(tableColumnSorts);
    }

    @Override
    public void updateBy(String formCode, String employeeCode, List<String> fieldCodes) {
        List<SysColumnSort> tableColumnSorts = dao.findByForm(formCode, employeeCode);
        Optional<SysColumnSort> tableColumnSort = tableColumnSorts.stream().max(Comparator.comparing(SysColumnSort::getSn));
        int maxSn = 1;
        if (tableColumnSort.isPresent()) {
            maxSn = tableColumnSort.get().getSn() + 1;
        }
        for (String fieldCode : fieldCodes) {
            boolean exists = tableColumnSorts.stream().anyMatch(s -> StringUtil.equals(s.getField().getCode(), fieldCode));
            if (!exists) {
                new AddSysColumnSortAction(employeeCode, fieldCode, maxSn).run();
                maxSn++;
            }
        }
    }

    @Override
    public void update(String employeeCode, List<SysColumnSortModifyVO> vos) {
        int sn = 1;
        for (SysColumnSortModifyVO vo : vos) {
            vo.setSn(sn++);
            new ModifySysColumnSortAction(employeeCode, vo).run();
        }
    }

    @Override
    public void update(String employeeCode, SysColumnSortModifyVO vo) {
        new ModifySysColumnSortAction(employeeCode, vo).run();
    }
}
