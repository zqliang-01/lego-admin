package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.TreeInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.system.action.AddSysDeptAction;
import com.lego.system.action.ModifySysDeptAction;
import com.lego.system.assembler.SysDeptAssembler;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.dto.SysDeptInfo;
import com.lego.system.entity.SysDept;
import com.lego.system.service.ISysDeptService;
import com.lego.system.vo.SysDeptCreateVO;
import com.lego.system.vo.SysDeptModifyVO;
import com.lego.system.vo.SysDeptSearchVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDeptService extends BusService<ISysDeptDao, SysDeptAssembler> implements ISysDeptService {

    @Override
    public List<SysDeptInfo> findBy(SysDeptSearchVO vo) {
        List<SysDept> page = dao.findBy(vo);
        return assembler.createTree(page);
    }

    @Override
    public void add(String operatorCode, SysDeptCreateVO vo) {
        new AddSysDeptAction(operatorCode, vo).run();
    }

    @Override
    public void modify(String operatorCode, SysDeptModifyVO vo) {
        new ModifySysDeptAction(operatorCode, vo).run();
    }

    @Override
    public void deleteBy(String code) {
        SysDept dept = dao.findByCode(code);
        dao.delete(dept);
    }

    @Override
    public List<TreeInfo> findTreeType() {
        return assembler.createTreeInfo(dao.findAll());
    }

    @Override
    public TypeInfo findSimpleTypeBy(String code) {
        SysDept dept = dao.findByCode(code);
        return assembler.createTypeInfo(dept);
    }

}
