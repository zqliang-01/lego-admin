package com.lego.system.service.impl;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.system.action.AddSysCustomFormAction;
import com.lego.system.action.DeleteSysCustomFormAction;
import com.lego.system.action.ModifySysCustomFormAction;
import com.lego.system.action.ModifySysCustomFormFieldAction;
import com.lego.system.assembler.SysCustomFormAssembler;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.service.ISysCustomFormService;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysCustomFormFieldModifyVO;
import com.lego.system.vo.SysCustomFormModifyVO;
import com.lego.system.vo.SysCustomFormSearchVO;

@Service
public class SysCustomFormService extends BusiService<ISysCustomFormDao, SysCustomFormAssembler> implements ISysCustomFormService {

	@Override
	public LegoPage<SysCustomFormInfo> findBy(SysCustomFormSearchVO vo) {
		LegoPage<SysCustomForm> page = dao.findBy(vo);
		return assembler.create(page);
	}

	@Override
	public void modifyField(String operatorCode, SysCustomFormFieldModifyVO vo) {
		new ModifySysCustomFormFieldAction(operatorCode, vo).run();
	}

	@Override
	public void modify(String operatorCode, SysCustomFormModifyVO vo) {
		new ModifySysCustomFormAction(operatorCode, vo).run();
	}

	@Override
	public void add(String operatorCode, SysCustomFormCreateVO vo) {
		new AddSysCustomFormAction(operatorCode, vo).run();
	}

	@Override
	public void delete(String operatorCode, String code) {
		new DeleteSysCustomFormAction(operatorCode, code).run();
	}

	@Override
	public String findAppCodeBy(String code) {
		SysCustomForm customForm = dao.findByCode(code);
		BusinessException.check(customForm.getPermission() != null, "表单[{0}]未配置菜单，查询表单信息失败！", customForm.getName());

		String permissionCode = customForm.getPermission().getCode();
		return permissionCode.split(":")[0];
	}

	@Override
	public String findCodeByPermission(String permissionCode) {
		SysCustomForm form = dao.findByPermission(permissionCode);
		BusinessException.check(form != null, "功能[{0}]未配置表单，获取表单信息失败！", permissionCode);
		return form.getCode();
	}

}
