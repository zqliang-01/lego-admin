package com.lego.core.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.core.data.TreeEntity;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.dto.DTO;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TreeInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.VersionDTO;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;

public abstract class BaseAssembler<D extends DTO, E extends BaseEntity> implements IBaseAssembler<D, E> {

	@Autowired
	protected TypeInfoAssembler typeInfoAssembler;

    @Override
    public List<D> create(List<E> entities) {
        List<D> infos = new ArrayList<D>();
        for (E entity : entities) {
            infos.add(create(entity));
        }
        return infos;
    }

    @Override
    public List<String> createCodes(List<E> entities) {
        List<String> codes = new ArrayList<String>();
        for (E entity : entities) {
        	codes.add(entity.getCode());
        }
    	return codes;
    }

    @Override
    public D create(E entity) {
    	D dto = doCreate(entity);
    	if (dto instanceof VersionDTO) {
    		VersionDTO versionDTO = (VersionDTO) dto;
    		versionDTO.setVersion(entity.getVersion());
    	}
		return dto;
    }

    protected abstract D doCreate(E entity);

	@Override
    public LegoPage<D> create(IPage<E> page) {
        return new LegoPage<D>(create(page.getRecords()), page.getCurrent(), page.getSize(), page.getTotal());
    }

	@Override
    public LegoPage<D> create(LegoPage<E> page) {
        return new LegoPage<D>(create(page.getResult()), page.getPageIndex(), page.getPageSize(), page.getTotalCount());
    }

	@Override
	public List<D> createTree(List<E> entities) {
		BusinessException.check(false, "未实现createTree！");
		return null;
	}

	@Override
	public TypeInfo createTypeInfo(BaseEntity entity) {
		return typeInfoAssembler.create(entity);
	}

	@Override
	public List<TypeInfo> createTypeInfo(Collection<? extends BaseEntity> entities) {
		return typeInfoAssembler.create(entities);
	}

	@Override
	public List<TreeInfo> createTreeInfo(List<? extends TreeEntity<E>> entities) {
		CoreException.check(false, "[{0}]未实现createTreeInfo", this.getClass().getSimpleName());
		return null;
	}
}
