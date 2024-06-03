package com.lego.core.common;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

public class BooleanConverter implements Converter<Boolean> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return Boolean.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.BOOLEAN;
	}

	@Override
	public WriteCellData<String> convertToExcelData(WriteConverterContext<Boolean> context) throws Exception {
		Boolean value = context.getValue();
		if (value == null || !value) {
			return new WriteCellData<String>("否");
		}
		return new WriteCellData<String>("是");
	}
}
