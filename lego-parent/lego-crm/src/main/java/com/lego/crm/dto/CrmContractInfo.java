package com.lego.crm.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.lego.core.common.TypeInfoConverter;
import com.lego.core.dto.BusDTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CrmContractInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "开始时间")
    @DateTimeFormat(DateUtil.datePattern)
    private Date startTime;
    @ExcelProperty(value = "结束时间")
    @DateTimeFormat(DateUtil.datePattern)
    private Date endTime;
    @ExcelProperty(value = "负责人", converter = TypeInfoConverter.class)
    private TypeInfo ownerCode;
    @ExcelProperty(value = "合同金额")
    private BigDecimal amount;
    @ExcelProperty(value = "线索", converter = TypeInfoConverter.class)
    private TypeInfo lead;
    @ExcelProperty(value = "客户", converter = TypeInfoConverter.class)
    private TypeInfo customer;
    @ExcelProperty(value = "类型", converter = TypeInfoConverter.class)
    private TypeInfo type;
}
