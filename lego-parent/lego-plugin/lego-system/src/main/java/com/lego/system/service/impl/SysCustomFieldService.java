package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.TypeInfo;
import com.lego.system.assembler.SysCustomFieldAssembler;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dto.SysCustomFieldInfo;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.service.ISysCustomFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysCustomFieldService extends BusService<ISysCustomFieldDao, SysCustomFieldAssembler> implements ISysCustomFieldService {

    @Autowired
    private ISysCustomFormDao formDao;

    @Autowired
    private ISysColumnSortDao columnSortDao;

    @Autowired
    private ISysGenTableColumnDao columnDao;

    @Override
    public List<SysCustomFieldInfo> findBy(String formCode) {
        List<SysCustomField> fields = dao.findBy(formCode);
        return assembler.create(fields);
    }

    @Override
    public List<TypeInfo> findSimpleTypeBy(String formCode) {
        List<SysCustomField> fields = dao.findBy(formCode);
        return fields.stream()
            .filter(field -> !field.isTips())
            .map(field -> new TypeInfo(field.getFieldCode(), field.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public List<SysCustomFieldInfo> findValidBy(String formCode) {
        List<SysCustomField> fields = dao.findValidBy(formCode);
        return assembler.create(fields);
    }

    @Override
    public List<SysCustomFieldInfo> findValidBy(String employeeCode, String formCode) {
        List<SysCustomField> fields = dao.findValidBy(formCode);
        List<SysColumnSort> columnSorts = columnSortDao.findByForm(formCode, employeeCode);
        return assembler.create(fields, columnSorts);
    }

    @Override
    public List<List<SysCustomFieldInfo>> findInit(String tableCode) {
        List<SysGenTableColumn> columns = columnDao.findBy(tableCode);
        List<SysCustomFieldInfo> fields = assembler.createInit(columns);
        List<List<SysCustomFieldInfo>> results = new ArrayList<List<SysCustomFieldInfo>>();
        List<SysCustomFieldInfo> result = null;
        for (int i = 0; i < fields.size(); ++i) {
            if (i % 2 == 0) {
                result = new ArrayList<SysCustomFieldInfo>();
                results.add(result);
            }
            SysCustomFieldInfo field = fields.get(i);
            field.setXAxis(i / 2);
            field.setYAxis(i % 2);
            result.add(field);
        }
        return results;
    }

}
