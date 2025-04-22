package com.lego.system.action;

import com.alibaba.fastjson2.JSON;
import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCodeGeneratorDao;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.vo.SysCustomFieldModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysCustomFieldAction extends ModifyAction<SysCustomField, ISysCustomFieldDao> {

    private SysCustomFieldModifyVO vo;

    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);
    private ISysCodeGeneratorDao generatorDao = getDao(ISysCodeGeneratorDao.class);

    public ModifySysCustomFieldAction(String operatorCode, SysCustomFieldModifyVO vo) {
        super(SysPermissionCode.manageCustomForm, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        if (!"descText".equals(vo.getFormType())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getFieldCode()), "表单项编码不能为空！");
            BusinessException.check(StringUtil.isNotBlank(vo.getName()), "表单项名称不能为空！");
        }
        BusinessException.check(StringUtil.isNotBlank(vo.getFormType()), "表单项类型不能为空！");
        this.setCheckDiff(false);
    }

    @Override
    protected void doModify(SysCustomField entity) {
        entity.setFieldCode(vo.getFieldCode());
        entity.setName(vo.getName());
        entity.setComponentName(vo.getComponentName());
        entity.setDefaultValue(getDefaultValue());
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
    }

    private String getDefaultValue() {
        Object defaultValue = vo.getDefaultValue();
        if (defaultValue != null && StringUtil.isNotBlank(defaultValue.toString())) {
            return JSON.toJSONString(defaultValue);
        }
        return null;
    }

    @Override
    protected void createLog() {
    }
}
