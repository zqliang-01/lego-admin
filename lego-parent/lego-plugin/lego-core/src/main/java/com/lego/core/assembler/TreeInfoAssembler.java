package com.lego.core.assembler;

import cn.hutool.core.map.MapUtil;
import com.lego.core.data.hibernate.entity.TreeEntity;
import com.lego.core.dto.TreeInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TreeInfoAssembler {

    public List<TreeInfo> create(List<? extends TreeEntity<?>> entities) {
        Map<Long, List<TreeInfo>> parentMap = MapUtil.newHashMap();
        Map<Long, TreeInfo> idMap = MapUtil.newHashMap();
        List<TreeInfo> result = new ArrayList<>();

        //按parent分组
        for (TreeEntity<?> entity : entities) {
            TreeInfo info = create(entity);
            idMap.put(entity.getId(), info);
            List<TreeInfo> parentEntities = parentMap.computeIfAbsent(entity.getParentId(), k -> new ArrayList<>());
            parentEntities.add(info);
        }
        for (HashMap.Entry<Long, List<TreeInfo>> entry : parentMap.entrySet()) {
            Long id = entry.getKey();
            List<TreeInfo> infos = entry.getValue();
            matchChildren(id, infos, idMap, result);
        }
        return result;
    }

    private TreeInfo create(TreeEntity<?> entity) {
        TreeInfo info = new TreeInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        return info;
    }

    private void matchChildren(Long key, List<TreeInfo> infos, Map<Long, TreeInfo> idMap, List<TreeInfo> result) {
        TreeInfo info = idMap.get(key);
        if (info != null) {
            for (TreeInfo dto : infos) {
                dto.setParentCode(info.getCode());
            }
            info.setChildrens(infos);
        } else {
            result.addAll(infos);
        }
    }
}
