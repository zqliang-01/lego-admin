package com.lego.crm.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.common.Constants;
import com.lego.core.module.flowable.FlowableCheckStatus;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.core.module.flowable.IFlowableCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.crm.service.ICrmLeadService;
import com.lego.crm.vo.CrmLeadCreateVO;
import com.lego.crm.vo.CrmLeadModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CrmLeadCompletedListener implements IFlowableCompletedListener {

    @Autowired
    private ICrmLeadService leadService;

    @Override
    public String taskCompleted(Map<String, Object> variables) {
        String code = StringUtil.toString(variables.get(FlowableProcessConstants.FORM_UNIQUE_KEY));
        if (StringUtil.isNotBlank(code) && leadService.exists(code)) {
            CrmLeadModifyVO vo = BeanUtil.toBean(variables, CrmLeadModifyVO.class);
            leadService.update(Constants.loginCode.get(), vo);
            return code;
        }
        CrmLeadCreateVO vo = BeanUtil.toBean(variables, CrmLeadCreateVO.class);
        return leadService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public void taskRejected(String code) {
        leadService.updateCheckStatus(code, FlowableCheckStatus.rejected);
    }

    @Override
    public void processCompleted(String code) {
        leadService.updateCheckStatus(code, FlowableCheckStatus.completed);
    }

    @Override
    public String getTableCode() {
        return "crm_lead";
    }
}