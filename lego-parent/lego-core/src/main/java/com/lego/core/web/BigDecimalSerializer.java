package com.lego.core.web;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class BigDecimalSerializer implements ObjectSerializer {

	public static final BigDecimalSerializer instance = new BigDecimalSerializer();

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
        	serializer.writeNull();
            return;
        }
        SerializeWriter out = serializer.out;
        BigDecimal val = (BigDecimal) object;
        int scale = val.scale();

        String outText;
        if (scale >= -100 && scale < 100) {
            outText = val.toPlainString();
        }
        else {
            outText = val.toString();
        }

        if (scale == 0) {
            if (outText.length() >= 16) {
                out.writeString(outText);
                return;
            }
        }

        out.write(outText);
    }

}
