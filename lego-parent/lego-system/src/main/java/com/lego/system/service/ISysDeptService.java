package com.lego.system.service;

import com.lego.core.dto.TreeInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysDeptInfo;
import com.lego.system.vo.SysDeptCreateVO;
import com.lego.system.vo.SysDeptModifyVO;
import com.lego.system.vo.SysDeptSearchVO;

import java.util.List;

public interface ISysDeptService {

    List<SysDeptInfo> findBy(SysDeptSearchVO vo);

    void add(String operatorCode, SysDeptCreateVO vo);

    void modify(String operatorCode, SysDeptModifyVO vo);

    void deleteBy(String code);

    TypeInfo findSimpleTypeBy(String code);

    List<TreeInfo> findTreeType();

    List<TypeInfo> findChildrenBy(String parentCode);
}
