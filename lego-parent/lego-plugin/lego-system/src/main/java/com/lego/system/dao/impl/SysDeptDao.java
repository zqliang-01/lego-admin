package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.entity.SysDept;
import com.lego.system.vo.SysDeptSearchVO;

import java.util.ArrayList;
import java.util.List;

public class SysDeptDao extends GenericDao<SysDept> implements ISysDeptDao {

    @Override
    public List<SysDept> findBy(SysDeptSearchVO vo) {
        QueryHandler<SysDept> query = createQueryHandler();
        if (StringUtil.isNotBlank(vo.getCode())) {
            query.condition("t.code = :code").param("code", vo.getCode());
        }
        if (StringUtil.isNotBlank(vo.getName())) {
            query.condition("t.name LIKE :name").param("name", "%" + vo.getName() + "%");
        }
        return query.findList();
    }

    @Override
    public List<SysDept> findBy(String parentCode) {
        QueryHandler<SysDept> query = createQueryHandler();
        if (StringUtil.isBlank(parentCode)) {
            query.condition("t.parent IS NULL");
        } else {
            query.condition("t.parent.code = :parentCode").param("parentCode", parentCode);
        }
        return query.findList();
    }

    @Override
    public List<String> findAllChildrenCode(SysDept dept) {
        List<String> codes = new ArrayList<>();
        for (SysDept child : dept.getChildren()) {
            codes.add(child.getCode());
            codes.addAll(findAllChildrenCode(child));
        }
        codes.add(dept.getCode());
        return codes;
    }

}
