package com.lego.crm.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.common.Constants;
import com.lego.core.flowable.FlowableCheckStatus;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.flowable.IFlowableTaskCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.crm.service.ICrmLeadService;
import com.lego.crm.vo.CrmLeadCreateVO;
import com.lego.crm.vo.CrmLeadModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CrmLeadCompletedListener implements IFlowableTaskCompletedListener {

    @Autowired
    private ICrmLeadService leadService;

    @Override
    public void completed(boolean isSave, Map<String, Object> variables) {
        String code = StringUtil.toString(variables.get(FlowableProcessConstants.FORM_UNIQUE_KEY));
        if (leadService.exists(code)) {
            CrmLeadModifyVO vo = BeanUtil.toBean(variables, CrmLeadModifyVO.class);
            vo.setCheckDiff(isSave);
            leadService.update(Constants.loginCode.get(), vo);
            return;
        }
        CrmLeadCreateVO vo = BeanUtil.toBean(variables, CrmLeadCreateVO.class);
        leadService.add(Constants.loginCode.get(), vo);
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
