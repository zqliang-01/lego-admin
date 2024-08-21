package com.lego.system.entity;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "sys_custom_field")
public class SysCustomField extends BaseEntity {

    private String fieldCode;
    private String componentName;
    private String defaultValue;
    private String formType;
    private String inputTips;
    private String stylePercent;
    private String optionDataType;
    private String optionDictType;
    private Integer precisions;
    private Integer maxNumRestrict;
    private Integer minNumRestrict;
    private String setting;
    private boolean hidden;
    private boolean required;
    private boolean uniqueness;
    private int xAxis;
    private int yAxis;
    private int sn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private SysCustomForm form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relative_form_id", referencedColumnName = "id")
    private SysCustomForm relativeForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generator_id", referencedColumnName = "id")
    private SysCodeGenerator codeGenerator;

    protected SysCustomField() {
    }

    public SysCustomField(String code, String name, SysCustomForm form) {
        super(code, name);
        this.form = form;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", getCode());
        attributes.put("字段", fieldCode);
        attributes.put("名称", getName());
        attributes.put("类型", formType);
        attributes.put("组件类型", componentName);
        attributes.put("自动编码器", EntityUtil.toString(codeGenerator));
        attributes.put("提示语", inputTips);
        attributes.put("占比", stylePercent);
        attributes.put("行号", StringUtil.toString(xAxis));
        attributes.put("列号", StringUtil.toString(yAxis));
        attributes.put("默认值", defaultValue);
        attributes.put("数据类型", optionDataType);
        attributes.put("字典类型", optionDictType);
        attributes.put("支持小数位", StringUtil.toString(precisions));
        attributes.put("最大值", StringUtil.toString(maxNumRestrict));
        attributes.put("最小值", StringUtil.toString(minNumRestrict));
        attributes.put("选项内容", setting);
        attributes.put("是否隐藏", hidden ? "是" : "否");
        attributes.put("是否必填", required ? "是" : "否");
        attributes.put("是否唯一", uniqueness ? "是" : "否");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isTips() {
        return CustomFieldTypeEnum.DESC_TEXT.equals(formType);
    }
}
