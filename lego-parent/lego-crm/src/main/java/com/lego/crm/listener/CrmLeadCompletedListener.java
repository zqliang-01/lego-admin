package com.lego.crm.listener;

import com.lego.core.common.Constants;
import com.lego.core.flowable.FlowableTaskCompletedListener;
import com.lego.crm.service.ICrmLeadService;
import com.lego.crm.vo.CrmLeadCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrmLeadCompletedListener extends FlowableTaskCompletedListener<CrmLeadCreateVO> {

    @Autowired
    private ICrmLeadService leadService;

    @Override
    public void doCompleted(CrmLeadCreateVO vo) {
        leadService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public String getTableCode() {
        return "crm_lead";
    }
}
