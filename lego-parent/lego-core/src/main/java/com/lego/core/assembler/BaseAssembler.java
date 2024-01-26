package com.lego.core.assembler;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.core.data.TreeEntity;
import com.lego.core.dto.DTO;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TreeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAssembler<D extends DTO, E> {

  public List<D> create(List<E> entities) {
    List<D> infos = new ArrayList<D>();
    for (E entity : entities) {
      infos.add(create(entity));
    }
    return infos;
  }

  public D create(E entity) {
    return doCreate(entity);
  }

  protected abstract D doCreate(E entity);

  public LegoPage<D> create(IPage<E> page) {
    return new LegoPage<D>(create(page.getRecords()), page.getCurrent(), page.getSize(), page.getTotal());
  }

  public LegoPage<D> create(LegoPage<E> page) {
    return new LegoPage<D>(create(page.getResult()), page.getPageIndex(), page.getPageSize(), page.getTotalCount());
  }

  public List<D> createTree(List<E> entities) {
    BusinessException.check(false, "未实现createTree！");
    return null;
  }

  public List<TreeInfo> createTreeInfo(List<? extends TreeEntity<?>> entities) {
    CoreException.check(false, "[{0}]未实现createTreeInfo", this.getClass().getSimpleName());
    return null;
  }
}
