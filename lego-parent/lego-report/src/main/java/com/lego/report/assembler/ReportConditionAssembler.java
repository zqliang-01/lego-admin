package com.lego.report.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.report.dto.ReportConditionInfo;
import com.lego.report.entity.ReportCondition;
import com.lego.report.vo.ReportConditionTypeCode;
import com.lego.report.vo.ReportConditionVO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportConditionAssembler extends EntityAssembler<ReportConditionInfo, ReportCondition> {

    @Override
    protected ReportConditionInfo doCreate(ReportCondition entity) {
        ReportConditionInfo info = new ReportConditionInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setSqlKey(entity.getSqlKey());
        info.setType(entity.getType());
        info.setSn(entity.getSn());
        info.setRequired(entity.isRequired());
        info.setDefaultValue(entity.getDefaultValue());
        info.setDependentCode(entity.getDependentCode());
        info.setDefinitionCode(EntityUtil.getCode(entity.getDefinition()));
        info.setDataDefinitionCode(EntityUtil.getCode(entity.getDataDefinition()));
        return info;
    }

    public Map<String, Object> convertParams(List<ReportConditionVO> params) {
        Map<String, Object> converted = new HashMap<String, Object>();
        for (ReportConditionVO param : params) {
            String code = param.getSqlKey();
            Object value = getRealValue(param.getType(), param.getName(), param.getDefaultValue());
            converted.put(code, value);
        }
        return converted;
    }

    public Map<String, Object> convertParams(List<ReportCondition> params, Map<String, String> values) {
        Map<String, Object> converted = new HashMap<String, Object>();
        for (ReportCondition param : params) {
            String key = param.getSqlKey();
            Object value = getRealValue(param.getType(), param.getName(), values.get(key));
            converted.put(key, value);
        }
        return converted;
    }

    private Object getRealValue(String type, String name, String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        }
        if (ReportConditionTypeCode.DATE.equals(type)) {
            return DateUtil.toDate(value);
        }
        if (ReportConditionTypeCode.BOOLEAN.equals(type)) {
            BusinessException.check(Arrays.asList("true", "false").contains(value), "布尔值只能为true或者false: " + name);
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}
