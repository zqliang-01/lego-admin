package com.lego.crm.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.module.gen.converter.TypeInfoConverter;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.BusDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmCustomerInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "手机号")
    private String mobile;
    @ExcelProperty(value = "网址")
    private String website;
    @ExcelProperty(value = "邮箱")
    private String email;
    @ExcelProperty(value = "类型", converter = TypeInfoConverter.class)
    private TypeInfo type;
}