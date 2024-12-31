package com.lego.core.gen.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.lego.core.data.hibernate.entity.AddressEntity;

public class AddressConverter implements Converter<AddressEntity> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return AddressEntity.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public WriteCellData<String> convertToExcelData(WriteConverterContext<AddressEntity> context) throws Exception {
		AddressEntity value = context.getValue();
		if (value == null) {
			return new WriteCellData<String>("");
		}
		return new WriteCellData<String>(value.toString());
	}
}
