package com.lego.crm.dto;

import java.math.BigDecimal;
import com.lego.core.data.hibernate.entity.AddressEntity;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.lego.core.module.gen.converter.AddressConverter;
import com.lego.core.module.gen.converter.TypeInfoConverter;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.BusDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmContractInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "开始时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ExcelProperty(value = "结束时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ExcelProperty(value = "负责人", converter = TypeInfoConverter.class)
    private TypeInfo owner;
    @ExcelProperty(value = "合同金额")
    private BigDecimal amount;
    @ExcelProperty(value = "类型", converter = TypeInfoConverter.class)
    private TypeInfo type;
    @ExcelProperty(value = "地址", converter = AddressConverter.class)
    private AddressEntity address;
    @ExcelProperty(value = "线索", converter = TypeInfoConverter.class)
    private TypeInfo lead;
    @ExcelProperty(value = "客户", converter = TypeInfoConverter.class)
    private TypeInfo customer;
}