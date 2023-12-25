package com.lego.system.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.system.assembler.SysCustomFieldAssembler;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.dto.SysCustomFieldInfo;
import com.lego.system.dto.SysTableColumnInfo;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.service.ISysCustomFieldService;

@Service
public class SysCustomFieldService extends BusiService<ISysCustomFieldDao, SysCustomFieldAssembler> implements ISysCustomFieldService {

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
	public List<TypeInfo> findGenColumnBy(String tableCode) {
		List<SysGenTableColumn> columns = columnDao.findBy(tableCode);
		List<TypeInfo> columnInfos = new ArrayList<TypeInfo>();
		for (SysGenTableColumn column : columns) {
			String code = column.getJavaField();
			String name = column.getComment();
			columnInfos.add(new TypeInfo(code, name));
		}
		return columnInfos;
	}

	@Override
	public SysTableColumnInfo findValidBy(String employeeCode, String formCode) {
		SysCustomForm form = formDao.findByCode(formCode);
		SysPermission permission = form.getPermission();
		BusinessException.check(permission != null, "表单[{0}]未设置关联功能菜单！", form.getName());

		List<SysCustomField> fields = dao.findValidBy(form.getCode());
		Collections.sort(fields);

		String permissionCode = permission.getCode();
		List<SysColumnSort> columnSorts = columnSortDao.findByPermission(permissionCode, employeeCode);
		List<SysCustomFieldInfo> fieldInfos = assembler.create(fields, columnSorts);
		return new SysTableColumnInfo(formCode, permissionCode, fieldInfos);
	}

	@Override
	public List<String> findCodesByPermission(String permissionCode) {
		SysCustomForm form = formDao.findByPermission(permissionCode);
		BusinessException.check(form != null, "功能菜单[{0}]未设置关联表单！", permissionCode);

		List<SysCustomField> fields = dao.findBy(form.getCode());
		Collections.sort(fields);
		return assembler.createCodes(fields);
	}

	@Override
	public List<List<SysCustomFieldInfo>> findInit(String appCode, String tableCode) {
		List<SysGenTableColumn> columns = columnDao.findBy(tableCode);
		List<SysCustomFieldInfo> fields = assembler.createInit(appCode, columns);
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
			field.resetFormPosition();
			result.add(field);
		}
		return results;
	}

}
