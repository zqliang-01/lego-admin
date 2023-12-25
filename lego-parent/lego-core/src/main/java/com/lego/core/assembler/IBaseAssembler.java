package com.lego.core.assembler;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.core.data.TreeEntity;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.dto.DTO;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TreeInfo;
import com.lego.core.dto.TypeInfo;

public interface IBaseAssembler<D extends DTO, E extends BaseEntity> {

    D create(E entity);

    List<D> create(List<E> entities);
    List<String> createCodes(List<E> entities);

    LegoPage<D> create(IPage<E> page);
    LegoPage<D> create(LegoPage<E> page);

    List<D> createTree(List<E> entities);

    TypeInfo createTypeInfo(BaseEntity entity);
    List<TypeInfo> createTypeInfo(Collection<? extends BaseEntity> entities);

    List<TreeInfo> createTreeInfo(List<? extends TreeEntity<E>> entities);
}
