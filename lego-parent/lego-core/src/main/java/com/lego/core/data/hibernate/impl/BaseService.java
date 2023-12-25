package com.lego.core.data.hibernate.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.lego.core.assembler.TypeInfoAssembler;
import com.lego.core.data.hibernate.ICommonDao;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.core.vo.GenericSearchConditionEnum;
import com.lego.core.vo.GenericSearchItemVO;
import com.lego.core.vo.GenericSearchSortEnum;
import com.lego.core.vo.GenericSearchVO;

@Transactional
public class BaseService {

	@Resource
	protected TypeInfoAssembler typeInfoAssembler;

    @Autowired
    protected ICommonDao commonDao;

    protected GenericConditionVO buildCondition(GenericSearchVO vo) {
		GenericConditionVO conditionVO = GenericConditionVO.create(vo);
    	String sortType = GenericSearchSortEnum.getSortType(vo.getOrderType());
    	if (StringUtil.isNotBlank(sortType) && StringUtil.isNotBlank(vo.getSortField())) {
    		conditionVO.setOrderType(vo.getSortField() + " " + sortType);
    	}
    	String searchValue = vo.getSearch();
		if (StringUtil.isNotBlank(searchValue)) {
    		conditionVO.addItem(new GenericConditionItemVO(GenericSearchConditionEnum.LIKE, "name", "%" + searchValue + "%"));
    	}
    	for (GenericSearchItemVO search : vo.getSearchList()) {
    		GenericSearchConditionEnum condition = GenericSearchConditionEnum.get(search.getType());
    		BusinessException.check(condition != null, "不存在的条件类型[{0}]，条件查询失败！", search.getType());

    		CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(search.getFormType());
    		BusinessException.check(fieldType != null, "不存在的字段类型[{0}]，条件查询失败！", search.getFormType());

    		Object value = search.getValue(fieldType);
    		if (condition.isSingleValue()) {
    			value = search.getFirstValue(fieldType);
    		}
    		if (GenericSearchConditionEnum.LIKE.equals(condition)) {
    			value = "%" + search.getFirstValue(fieldType) + "%";
    		}
    		if (!condition.isNotValue() && value == null) {
    			throw new BusinessException("条件类型[{0}]需要传值，但是值为空！", condition.getName());
    		}
    		conditionVO.addItem(new GenericConditionItemVO(condition, fieldType, search.getFieldCode(), value));
    	}
    	return conditionVO;
    }
}
