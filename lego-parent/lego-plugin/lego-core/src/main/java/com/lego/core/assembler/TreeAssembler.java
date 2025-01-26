package com.lego.core.assembler;

import cn.hutool.core.map.MapUtil;
import com.lego.core.data.hibernate.entity.TreeEntity;
import com.lego.core.dto.TreeDTO;
import com.lego.core.dto.TreeInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TreeAssembler<D extends TreeDTO<D>, E extends TreeEntity<E>> extends EntityAssembler<D, E> {

    @Autowired
    protected TreeInfoAssembler treeInfoAssembler;

    @Override
    public List<D> createTree(List<E> entities) {
        Map<Long, List<D>> parentMap = MapUtil.newHashMap();
        Map<Long, D> idMap = MapUtil.newHashMap();
        List<D> result = new ArrayList<>();
        //按parent分组
        for (E entity : entities) {
            D info = create(entity);
            idMap.put(entity.getId(), info);
            List<D> parentEntities = parentMap.computeIfAbsent(entity.getParentId(), k -> new ArrayList<>());
            parentEntities.add(info);
        }
        for (HashMap.Entry<Long, List<D>> entry : parentMap.entrySet()) {
            Long id = entry.getKey();
            List<D> infos = entry.getValue();
            matchChildren(id, infos, idMap, result);
        }
        return result;
    }

    private void matchChildren(Long id, List<D> infos, Map<Long, D> idMap, List<D> result) {
        D info = idMap.get(id);
        if (info != null) {
            for (D dto : infos) {
                dto.setParentCode(info.getCode());
            }
            info.setChildrens(infos);
        } else {
            result.addAll(infos);
        }
    }

    public List<TreeInfo> createTreeInfo(List<? extends TreeEntity<?>> entities) {
        return treeInfoAssembler.create(entities);
    }
}
