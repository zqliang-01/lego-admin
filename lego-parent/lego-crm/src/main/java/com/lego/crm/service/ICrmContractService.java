package com.lego.crm.service;

import java.util.List;

import com.lego.core.data.hibernate.IBusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.dto.CrmContractInfo;
import com.lego.crm.vo.CrmContractCreateVO;
import com.lego.crm.vo.CrmContractModifyVO;

public interface ICrmContractService extends IBusService {

    LegoPage<CrmContractInfo> findPageBy(GenericSearchVO vo);

    CrmContractInfo findBy(String code);

    TypeInfo findSimpleBy(String code);

    List<CrmContractInfo> findBy(GenericSearchVO vo);

    List<CrmContractInfo> findBy(List<String> codes);

    void update(String operatorCode, CrmContractModifyVO vo);

    String add(String operatorCode, CrmContractCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}