package com.lego.system.assembler;

import com.alibaba.fastjson2.JSON;
import com.lego.core.assembler.EntityAssembler;
import com.lego.core.enums.FieldTypeEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dto.SysCustomFieldInfo;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import com.lego.system.entity.SysGenTableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class SysCustomFieldAssembler extends EntityAssembler<SysCustomFieldInfo, SysCustomField> {

    @Autowired
    private ISysCustomFormDao formDao;

    @Override
    protected SysCustomFieldInfo doCreate(SysCustomField entity) {
        SysCustomFieldInfo info = new SysCustomFieldInfo();
        info.setCode(entity.getCode());
        info.setFieldCode(entity.getFieldCode());
        info.setName(entity.getName());
        info.setComponentName(entity.getComponentName());
        info.setDefaultValue(buildDefaultValue(entity.getDefaultValue(), entity.getFormType()));
        info.setFormType(entity.getFormType());
        info.setHidden(entity.isHidden());
        info.setInputTips(entity.getInputTips());
        info.setRequired(entity.isRequired());
        info.setOptionDataType(entity.getOptionDataType());
        info.setOptionDictType(entity.getOptionDictType());
        info.setPrecisions(entity.getPrecisions());
        info.setMaxNumRestrict(entity.getMaxNumRestrict());
        info.setMinNumRestrict(entity.getMinNumRestrict());
        info.setSetting(JSON.parse(entity.getSetting()));
        info.setStylePercent(entity.getStylePercent());
        info.setUnique(entity.isUniqueness());
        info.setXAxis(entity.getXAxis());
        info.setYAxis(entity.getYAxis());
        info.setRelativeForm(createTypeInfo(entity.getRelativeForm()));
        info.setCodeGenerator(createTypeInfo(entity.getCodeGenerator()));
        return info;
    }

    private Object buildDefaultValue(String defaultValue, String formType) {
        if (StringUtil.isBlank(defaultValue)) {
            return null;
        }
        FieldTypeEnum fieldType = FieldTypeEnum.get(formType);
        if (fieldType != null) {
            return fieldType.parseDefaultValue(defaultValue);
        }
        return JSON.parseObject(defaultValue, String.class);
    }

    public List<SysCustomFieldInfo> create(List<SysCustomField> fields, List<SysColumnSort> columnSorts) {
        List<SysCustomFieldInfo> infos = new ArrayList<SysCustomFieldInfo>();
        for (SysColumnSort columnSort : columnSorts) {
            Optional<SysCustomField> field = fields.stream().filter(f -> f.equals(columnSort.getField())).findFirst();
            if (field.isPresent()) {
                if (columnSort.isVisible()) {
                    SysCustomFieldInfo info = create(field.get());
                    info.setWidth(columnSort.getWidth());
                    info.setSortCode(columnSort.getCode());
                    infos.add(info);
                }
                fields.remove(field.get());
            }
        }
        infos.addAll(create(fields));
        return infos;
    }

    public List<SysCustomFieldInfo> createInit(List<SysGenTableColumn> columns) {
        List<SysCustomFieldInfo> infos = new ArrayList<SysCustomFieldInfo>();
        for (SysGenTableColumn column : columns) {
            FieldTypeEnum fieldType = FieldTypeEnum.get(column.getFormType());
            if (fieldType == null) {
                continue;
            }
            SysCustomFieldInfo info = new SysCustomFieldInfo();
            SysGenTable relativeTable = column.getRelativeTable();
            if (fieldType == FieldTypeEnum.ENTITY) {
                BusinessException.check(relativeTable != null, "关联表类型字段[{0}]未选择关联的数据表！", column.getName());
                SysCustomForm form = formDao.findByTable(relativeTable);
                if (form != null) {
                    info.setRelativeForm(createTypeInfo(form));
                }
            }
            if (fieldType == FieldTypeEnum.ADDRESS) {
                info.setDefaultValue(buildDefaultValue("{}", column.getFormType()));
            }
            info.setUnique(column.isUniqueness());
            info.setRequired(column.isRequired());
            info.setFieldCode(column.getJavaField());
            info.setComponentName(fieldType.getComponentName());
            info.setFormType(fieldType.getCode());
            info.setName(column.getComment());
            info.setOptionDataType("dict");
            info.setSetting(Arrays.asList());
            info.setStylePercent("50");
            infos.add(info);
        }
        return infos;
    }
}
