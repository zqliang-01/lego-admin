package com.lego.crm.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.dto.CrmContractInfo;
import com.lego.crm.vo.CrmContractCreateVO;
import com.lego.crm.vo.CrmContractModifyVO;

public interface ICrmContractService {

    LegoPage<CrmContractInfo> findPageBy(GenericSearchVO vo);

    CrmContractInfo findBy(String code);

    List<CrmContractInfo> findBy(GenericSearchVO vo);

    List<CrmContractInfo> findBy(List<String> codes);

    void update(String operatorCode, CrmContractModifyVO vo);

    void add(String operatorCode, CrmContractCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}
