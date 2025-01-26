package com.lego.sharding.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.dto.BusDTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.module.gen.converter.BooleanConverter;
import com.lego.core.module.gen.converter.TypeInfoConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingTemplateInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "状态", converter = BooleanConverter.class)
    private boolean enable;
    @ExcelProperty(value = "描述")
    private String description;
    @ExcelProperty(value = "模板内容")
    private String json;
    @ExcelProperty(value = "模板类型", converter = TypeInfoConverter.class)
    private TypeInfo type;
}
