package com.lego.system.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.module.gen.converter.TypeInfoConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPrintTemplateInfo extends DTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "工号")
    private String code;
    @ExcelProperty(value = "姓名")
    private String name;
    @ExcelProperty(value = "模板内容")
    private String content;
    @ExcelProperty(value = "创建人", converter = TypeInfoConverter.class)
    private TypeInfo creator;
    @ExcelProperty(value = "关联表单", converter = TypeInfoConverter.class)
    private TypeInfo form;
}
