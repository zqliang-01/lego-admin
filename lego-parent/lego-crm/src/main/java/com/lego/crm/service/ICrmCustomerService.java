package com.lego.crm.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.dto.CrmCustomerInfo;
import com.lego.crm.vo.CrmCustomerCreateVO;
import com.lego.crm.vo.CrmCustomerModifyVO;

public interface ICrmCustomerService {

    LegoPage<CrmCustomerInfo> findPageBy(GenericSearchVO vo);

    CrmCustomerInfo findBy(String code);

    List<CrmCustomerInfo> findBy(GenericSearchVO vo);

    List<CrmCustomerInfo> findBy(List<String> codes);

    void update(String operatorCode, CrmCustomerModifyVO vo);

    void add(String operatorCode, CrmCustomerCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}
