package com.lego.core.data;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.dto.TypeInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceConfig {

    private Map<String, TypeInfo> datasource;

    public List<TypeInfo> getNames() {
        List<TypeInfo> infos = new ArrayList<TypeInfo>();
        if (CollectionUtil.isEmpty(datasource)) {
            infos.add(new TypeInfo("", "默认"));
            return infos;
        }
        for (Map.Entry<String, TypeInfo> ds : datasource.entrySet()) {
            infos.add(new TypeInfo(ds.getKey(), ds.getValue().getName()));
        }
        return infos;
    }
}
