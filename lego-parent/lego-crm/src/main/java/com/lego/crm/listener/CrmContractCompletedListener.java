package com.lego.crm.listener;

import com.lego.core.common.Constants;
import com.lego.core.flowable.FlowableTaskCompletedListener;
import com.lego.crm.service.ICrmContractService;
import com.lego.crm.vo.CrmContractCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrmContractCompletedListener extends FlowableTaskCompletedListener<CrmContractCreateVO> {

    @Autowired
    private ICrmContractService contractService;

    @Override
    public void doCompleted(CrmContractCreateVO vo) {
        contractService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public String getFormKey() {
        return "crm_contract_form";
    }
}
