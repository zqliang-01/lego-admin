package com.lego.crm.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.common.Constants;
import com.lego.core.module.flowable.FlowableCheckStatus;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.core.module.flowable.IFlowableCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.crm.service.ICrmCustomerService;
import com.lego.crm.vo.CrmCustomerCreateVO;
import com.lego.crm.vo.CrmCustomerModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CrmCustomerCompletedListener implements IFlowableCompletedListener {

    @Autowired
    private ICrmCustomerService customerService;

    @Override
    public String taskCompleted(Map<String, Object> variables) {
        String code = StringUtil.toString(variables.get(FlowableProcessConstants.FORM_UNIQUE_KEY));
        if (StringUtil.isNotBlank(code) && customerService.exists(code)) {
            CrmCustomerModifyVO vo = BeanUtil.toBean(variables, CrmCustomerModifyVO.class);
            vo.setCheckDiff(false);
            customerService.update(Constants.loginCode.get(), vo);
            return code;
        }
        CrmCustomerCreateVO vo = BeanUtil.toBean(variables, CrmCustomerCreateVO.class);
        vo.setCheckStatus(FlowableCheckStatus.checking);
        return customerService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public void taskRejected(String code) {
        customerService.updateCheckStatus(code, FlowableCheckStatus.rejected);
    }

    @Override
    public void processCompleted(String code) {
        customerService.updateCheckStatus(code, FlowableCheckStatus.completed);
    }

    @Override
    public String getTableCode() {
        return "crm_customer";
    }
}