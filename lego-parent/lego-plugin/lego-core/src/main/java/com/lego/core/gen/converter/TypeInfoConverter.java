package com.lego.core.gen.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.StringUtil;

public class TypeInfoConverter implements Converter<TypeInfo> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return TypeInfo.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public WriteCellData<String> convertToExcelData(WriteConverterContext<TypeInfo> context) throws Exception {
		TypeInfo value = context.getValue();
		if (value == null || StringUtil.isBlank(value.getName())) {
			return new WriteCellData<String>("");
		}
		return new WriteCellData<String>(value.getName());
	}
}
