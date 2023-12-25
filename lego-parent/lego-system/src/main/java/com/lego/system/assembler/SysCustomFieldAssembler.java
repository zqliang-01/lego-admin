package com.lego.system.assembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lego.core.assembler.BaseAssembler;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dto.SysCustomFieldInfo;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.entity.SysPermission;

@Component
public class SysCustomFieldAssembler extends BaseAssembler<SysCustomFieldInfo, SysCustomField> {

	@Autowired
	private ISysCustomFormDao formDao;

	@Override
	protected SysCustomFieldInfo doCreate(SysCustomField entity) {
		SysCustomFieldInfo info = new SysCustomFieldInfo();
		info.setCode(entity.getCode());
		info.setFieldCode(entity.getFieldCode());
		info.setName(entity.getName());
		info.setComponentName(entity.getComponentName());
		info.setDefaultValue(buildDefaultValue(entity));
		info.setFormPosition(entity.getFormPosition());
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
		info.setAppCode(parseAppCode(entity));
		info.setRelativeForm(createTypeInfo(entity.getRelativeForm()));
		info.setCodeGenerator(createTypeInfo(entity.getCodeGenerator()));
		return info;
	}

	private Object buildDefaultValue(SysCustomField entity) {
		String defaultValue = entity.getDefaultValue();
		if (StringUtil.isBlank(defaultValue)) {
			return defaultValue;
		}
		return JSON.parseObject(defaultValue, CustomFieldTypeEnum.getType(entity.getFormType()));
	}

	private String parseAppCode(SysCustomField entity) {
		SysCustomForm form = entity.getForm();
		SysPermission permission = form.getPermission();
		BusinessException.check(permission != null, "表单[{0}]未配置功能菜单，获取应用类型失败！", form.getName());

		String permissionCode = permission.getCode();
		return permissionCode.split(":")[0];
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

	public List<SysCustomFieldInfo> createInit(String appCode, List<SysGenTableColumn> columns) {
		List<SysCustomFieldInfo> infos = new ArrayList<SysCustomFieldInfo>();
		for (SysGenTableColumn column : columns) {
			CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(column.getFormType());
			if (fieldType == null) {
				continue;
			}
			SysCustomFieldInfo info = new SysCustomFieldInfo();
			SysGenTable relativeTable = column.getRelativeTable();
			if (fieldType == CustomFieldTypeEnum.ENTITY) {
				BusinessException.check(relativeTable != null, "关联表类型字段[{0}]未选择关联的数据表！", column.getName());
				SysCustomForm form = formDao.findByTable(relativeTable);
				if (form != null) {
					info.setRelativeForm(createTypeInfo(form));
				}
			}
			info.setUnique(column.isUniqueness());
			info.setRequired(column.isRequired());
			info.setAppCode(appCode);
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
