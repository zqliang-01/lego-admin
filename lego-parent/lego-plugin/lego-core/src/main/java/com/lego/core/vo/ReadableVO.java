package com.lego.core.vo;

import com.lego.core.dto.TypeInfo;
import com.lego.core.enums.PublicTypeEnum;
import com.lego.core.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ReadableVO {

    private Map<String, Object> map = new LinkedHashMap<String, Object>();

    public Object put(String key, String value) {
        return map.put(key, value);
    }

    public Object put(String key, String value, PublicTypeEnum type) {
        return map.put(key, new InnerValue(value, type));
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public String get(String key) {
        Object value = map.get(key);
        if (value instanceof InnerValue) {
            InnerValue innerValue = (InnerValue) value;
            TypeInfo info = innerValue.getTypeInfo();
            if (info == null) {
                return "";
            }
            return info.toString();
        }
        return StringUtil.toString(value);
    }

    public String getValue(String key) {
        Object value = map.get(key);
        if (value instanceof InnerValue) {
            InnerValue innerValue = (InnerValue) value;
            return innerValue.getValue();
        }
        return StringUtil.toString(value);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String key : map.keySet()) {
            sb.append(StringUtil.format("{0}:{1}\r\n", key, get(key)));
            i++;
            if (i < map.keySet().size()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    class InnerValue {
        private String value;
        private PublicTypeEnum type;

        public InnerValue(String value, PublicTypeEnum type) {
            this.value = value;
            this.type = type;
        }

        public TypeInfo getTypeInfo() {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            return type.getValue(value);
        }

        public String getValue() {
            return value;
        }
    }
}
