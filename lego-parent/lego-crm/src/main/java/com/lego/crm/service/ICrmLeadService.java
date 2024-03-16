package com.lego.crm.service;

import com.lego.core.data.hibernate.IBusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.dto.CrmLeadInfo;
import com.lego.crm.vo.CrmLeadCreateVO;
import com.lego.crm.vo.CrmLeadModifyVO;

import java.util.List;

public interface ICrmLeadService extends IBusService {

    LegoPage<CrmLeadInfo> findPageBy(GenericSearchVO vo);

    CrmLeadInfo findBy(String code);

    TypeInfo findSimpleBy(String code);

    List<CrmLeadInfo> findBy(GenericSearchVO vo);

    List<CrmLeadInfo> findBy(List<String> codes);

    void update(String operatorCode, CrmLeadModifyVO vo);

    void add(String operatorCode, CrmLeadCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}
