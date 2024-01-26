package com.lego.core.assembler;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.VersionDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class EntityAssembler<D extends DTO, E extends BaseEntity> extends BaseAssembler<D, E> {

  @Autowired
  protected TypeInfoAssembler typeInfoAssembler;

  public List<String> createCodes(List<E> entities) {
    List<String> codes = new ArrayList<String>();
    for (E entity : entities) {
      codes.add(entity.getCode());
    }
    return codes;
  }

  public D create(E entity) {
    D dto = doCreate(entity);
    if (dto instanceof VersionDTO) {
      VersionDTO versionDTO = (VersionDTO) dto;
      versionDTO.setVersion(entity.getVersion());
    }
    return dto;
  }

  public TypeInfo createTypeInfo(BaseEntity entity) {
    return typeInfoAssembler.create(entity);
  }

  public List<TypeInfo> createTypeInfo(Collection<? extends BaseEntity> entities) {
    return typeInfoAssembler.create(entities);
  }

}
