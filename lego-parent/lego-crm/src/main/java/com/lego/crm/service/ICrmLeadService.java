package com.lego.crm.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.dto.CrmLeadInfo;
import com.lego.crm.vo.CrmLeadCreateVO;
import com.lego.crm.vo.CrmLeadModifyVO;

public interface ICrmLeadService {

    LegoPage<CrmLeadInfo> findPageBy(GenericSearchVO vo);

    CrmLeadInfo findBy(String code);

    List<CrmLeadInfo> findBy(GenericSearchVO vo);

    List<CrmLeadInfo> findBy(List<String> codes);

    void update(String operatorCode, CrmLeadModifyVO vo);

    void add(String operatorCode, CrmLeadCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}
