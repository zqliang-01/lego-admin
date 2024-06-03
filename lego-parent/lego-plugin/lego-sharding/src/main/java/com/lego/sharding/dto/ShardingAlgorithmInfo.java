package com.lego.sharding.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.common.BooleanConverter;
import com.lego.core.common.TypeInfoConverter;
import com.lego.core.dto.BusDTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingAlgorithmInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "生效", converter = BooleanConverter.class)
    private boolean enable;
    @ExcelProperty(value = "描述")
    private String description;
    @ExcelProperty(value = "模板", converter = TypeInfoConverter.class)
    private TypeInfo template;
    @ExcelProperty(value = "规则", converter = TypeInfoConverter.class)
    private TypeInfo config;
}
