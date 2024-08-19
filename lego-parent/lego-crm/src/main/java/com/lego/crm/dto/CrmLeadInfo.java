package com.lego.crm.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.lego.core.util.DateUtil;
import com.lego.core.common.BooleanConverter;
import com.lego.core.common.TypeInfoConverter;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.BusDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmLeadInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "手机号码")
    private String mobile;
    @ExcelProperty(value = "金额")
    private BigDecimal amount;
    @ExcelProperty(value = "生日")
    @DateTimeFormat(DateUtil.datePattern)
    private Date brithday;
    @ExcelProperty(value = "地址")
    private String address;
    @ExcelProperty(value = "年龄")
    private int age;
    @ExcelProperty(value = "百分数")
    private String size;
    @ExcelProperty(value = "状态", converter = BooleanConverter.class)
    private boolean status;
    @ExcelProperty(value = "邮箱")
    private String email;
    @ExcelProperty(value = "员工", converter = TypeInfoConverter.class)
    private TypeInfo employee;
    @ExcelProperty(value = "部门", converter = TypeInfoConverter.class)
    private TypeInfo dept;
    @ExcelProperty(value = "来源", converter = TypeInfoConverter.class)
    private TypeInfo source;
    @ExcelProperty(value = "客户", converter = TypeInfoConverter.class)
    private TypeInfo customer;
}
