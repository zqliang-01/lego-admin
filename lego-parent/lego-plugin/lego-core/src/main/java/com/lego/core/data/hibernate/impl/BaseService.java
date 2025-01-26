package com.lego.core.data.hibernate.impl;

import com.lego.core.data.hibernate.ICommonDao;
import com.lego.core.enums.FieldTypeEnum;
import com.lego.core.enums.GenericConditionEnum;
import com.lego.core.enums.GenericSortEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.module.gen.handler.IFieldTypeHandler;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.core.vo.GenericSearchItemVO;
import com.lego.core.vo.GenericSearchVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseService {

    @Autowired
    protected ICommonDao commonDao;

    protected GenericConditionVO buildCondition(GenericSearchVO vo) {
        GenericConditionVO conditionVO = GenericConditionVO.create(vo);
        String sortType = GenericSortEnum.getSortType(vo.getOrderType());
        if (StringUtil.isNotBlank(sortType) && StringUtil.isNotBlank(vo.getSortField())) {
            conditionVO.setOrderType(vo.getSortField() + " " + sortType);
        }
        String searchValue = vo.getSearch();
        if (StringUtil.isNotBlank(searchValue)) {
            conditionVO.addItem(new GenericConditionItemVO(GenericConditionEnum.LIKE, "name", "%" + searchValue + "%"));
        }
        for (GenericSearchItemVO search : vo.getSearchList()) {
            GenericConditionEnum condition = GenericConditionEnum.get(search.getType());
            BusinessException.check(condition != null, "不存在的条件类型[{0}]，条件查询失败！", search.getType());

            List<String> values = search.getValues();
            if (condition.isSingleValue()) {
                int conditionSize = values.size();
                BusinessException.check(conditionSize == 1, "条件类型[{0}]只支持单个条件查询，当前查询条件数量为[{1}]！", search.getType(), conditionSize);
            }
            FieldTypeEnum fieldType = FieldTypeEnum.get(search.getFormType());
            BusinessException.check(fieldType != null, "不支持的字段类型[{0}]，条件查询失败！", search.getFormType());

            IFieldTypeHandler handler = fieldType.getHandler();
            conditionVO.addItem(handler.buildCondition(condition, values, search.getFieldCode()));
        }
        return conditionVO;
    }
}
