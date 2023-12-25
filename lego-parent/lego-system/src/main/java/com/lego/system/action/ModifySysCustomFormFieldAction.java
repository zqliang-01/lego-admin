package com.lego.system.action;

import java.util.List;
import java.util.Optional;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.vo.SysCustomFieldModifyVO;
import com.lego.system.vo.SysCustomFormFieldModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysCustomFormFieldAction extends MaintainAction {

	private SysCustomFormFieldModifyVO vo;

	private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);
	private ISysCustomFieldDao fieldDao = getDao(ISysCustomFieldDao.class);

	public ModifySysCustomFormFieldAction(String operatorCode, SysCustomFormFieldModifyVO vo) {
		super(SysPermissionCode.manageCustomForm, operatorCode);
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(!vo.getData().isEmpty(), "表单项不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getFormCode()), "请选择需要修改的表单信息！");
	}

	@Override
	protected void doRun() {
		SysCustomForm entity = formDao.findByCode(vo.getFormCode());
		StringBuilder sb = new StringBuilder();
		List<SysCustomField> fields = fieldDao.findBy(entity.getCode());
		createField(sb, entity, fields);
		modifyField(sb, fields);
		if (sb.length() == 0) {
			sb.append("内容无变化！");
		}
		this.description = "设计表单：" + entity.getName() + "\r\n" + sb.toString();
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.MODIFY;
	}

	private void modifyField(StringBuilder sb, List<SysCustomField> fields) {
		for (SysCustomField field : fields) {
			SysCustomFieldModifyVO fieldVO = vo.getFieldVO(field.getCode());
			if (fieldVO != null) {
				String description = new ModifySysCustomFieldAction(operatorCode, fieldVO).run();
				if (StringUtil.isNotBlank(description)) {
					sb.append("\r\n");
					sb.append("修改表单项：").append(field.getName()).append("\r\n");
					sb.append(description);
				}
				continue;
			}
			String description = new DeleteSysCustomFieldAction(operatorCode, field.getCode()).run();
			sb.append("\r\n");
			sb.append(description).append("\r\n");
		}
	}

	private void createField(StringBuilder sb, SysCustomForm entity, List<SysCustomField> fields) {
		for (SysCustomFieldModifyVO fieldVO : vo.getData()) {
			Optional<SysCustomField> field = fields.stream().filter(f -> f.getCode().equals(fieldVO.getCode())).findFirst();
			if (!field.isPresent()) {
				String description = new AddSysCustomFieldAction(operatorCode, entity, fieldVO).run();
				sb.append(description).append("\r\n");
			}
		}
	}

	@Override
	protected void createLog() {
		if (StringUtil.isNotBlank(description)) {
			super.createLog();
		}
	}

}
