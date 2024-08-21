package com.lego.system.action;

import com.alibaba.fastjson.JSON;
import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.system.dao.ISysCodeGeneratorDao;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.vo.SysCustomFieldModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysCustomFieldAction extends AddAction<SysCustomField, ISysCustomFieldDao> {

    private SysCustomForm form;
    private SysCustomFieldModifyVO vo;
    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);
    private ISysCodeGeneratorDao generatorDao = getDao(ISysCodeGeneratorDao.class);

    public AddSysCustomFieldAction(String operatorCode, SysCustomForm form, SysCustomFieldModifyVO vo) {
        super(SysPermissionCode.manageCustomForm, operatorCode);
        this.vo = vo;
        this.form = form;
    }

    @Override
    protected void preprocess() {
        if (!CustomFieldTypeEnum.DESC_TEXT.equals(vo.getFormType())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getFieldCode()), "表单项编码不能为空！");
            BusinessException.check(StringUtil.isNotBlank(vo.getName()), "表单项名称不能为空！");
        }
        SysCustomField field = entityDao.findByUnsureCode(vo.getCode());
        BusinessException.check(field == null, "表单[{0}]已存在表单项[{1}]，表单项添加失败！", form.getName(), vo.getName());
    }

    @Override
    protected SysCustomField createTargetEntity() {
        SysCustomField entity = new SysCustomField(vo.getCode(), vo.getName(), form);
        entity.setFieldCode(vo.getFieldCode());
        entity.setComponentName(vo.getComponentName());
        entity.setDefaultValue(JSON.toJSONString(vo.getDefaultValue()));
        entity.setFormType(vo.getFormType());
        entity.setHidden(vo.isHidden());
        entity.setInputTips(vo.getInputTips());
        entity.setRequired(vo.isRequired());
        entity.setOptionDataType(vo.getOptionDataType());
        entity.setOptionDictType(vo.getOptionDictType());
        entity.setPrecisions(vo.getPrecisions());
        entity.setMaxNumRestrict(vo.getMaxNumRestrict());
        entity.setMinNumRestrict(vo.getMinNumRestrict());
        entity.setSetting(vo.getSetting());
        entity.setStylePercent(vo.getStylePercent());
        entity.setUniqueness(vo.isUnique());
        entity.setXAxis(vo.getXAxis());
        entity.setYAxis(vo.getYAxis());
        entity.setSn(vo.getSn());
        entity.setRelativeForm(formDao.findByUnsureCode(vo.getRelativeFormCode()));
        entity.setCodeGenerator(generatorDao.findByUnsureCode(vo.getGeneratorCode()));
        return entity;
    }

    @Override
    protected void createLog() {
    }
}
