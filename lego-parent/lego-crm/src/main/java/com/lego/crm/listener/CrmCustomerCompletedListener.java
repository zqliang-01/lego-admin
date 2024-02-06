package com.lego.crm.listener;

import com.lego.core.common.Constants;
import com.lego.core.flowable.FlowableTaskCompletedListener;
import com.lego.crm.service.ICrmCustomerService;
import com.lego.crm.vo.CrmCustomerCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrmCustomerCompletedListener extends FlowableTaskCompletedListener<CrmCustomerCreateVO> {

    @Autowired
    private ICrmCustomerService customerService;

    @Override
    public void doCompleted(CrmCustomerCreateVO vo) {
        customerService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public String getFormKey() {
        return "crm_customer_form";
    }
}
