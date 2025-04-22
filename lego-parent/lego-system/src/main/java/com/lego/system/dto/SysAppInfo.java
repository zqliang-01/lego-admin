package com.lego.system.dto;

import java.util.HashMap;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.lego.core.dto.DTO;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysAppInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String icon;
	private String path;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SysAppInfo)) {
            return false;
        }
        SysAppInfo info = (SysAppInfo) obj;
        return StringUtil.equals(info.getCode(), code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }


    public static void main(String[] args) {
		String json = "{\"key1\":\"悟空\",\"key2\":{\"map\": \"八戒\"},\"key3\":\"沙僧\"}";
		HashMap<String,Object> map = JSONObject.parseObject(json, new TypeReference<HashMap<String,Object>>(){});
		System.out.println(map);
	}
}
