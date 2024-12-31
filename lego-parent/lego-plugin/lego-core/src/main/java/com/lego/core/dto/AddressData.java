package com.lego.core.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lego.core.util.StringUtil;

public class AddressData {

    private static JSONArray data;

    public static void init(String dataStr) {
        data = JSONObject.parseArray(dataStr);
    }

    public static String getName(String code) {
        return getName(code, data);
    }
    public static String getName(String code, JSONArray data) {
        String name = "";
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            String dataCode = jsonObject.getString("code");
            if (StringUtil.equals(code, dataCode)) {
                return jsonObject.getString("name");
            }
            JSONArray array = jsonObject.getJSONArray("children");
            if (StringUtil.isBlank(name) && array != null) {
                name = getName(code, array);
            }
        }
        return name;
    }
}
