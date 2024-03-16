package com.lego.crm.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.action.AddCrmLeadAction;
import com.lego.crm.action.DeleteCrmLeadAction;
import com.lego.crm.action.ModifyCrmLeadAction;
import com.lego.crm.assembler.CrmLeadAssembler;
import com.lego.crm.dao.ICrmLeadDao;
import com.lego.crm.dto.CrmLeadInfo;
import com.lego.crm.entity.CrmLead;
import com.lego.crm.service.ICrmLeadService;
import com.lego.crm.vo.CrmLeadCreateVO;
import com.lego.crm.vo.CrmLeadModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmLeadService extends BusService<ICrmLeadDao, CrmLeadAssembler> implements ICrmLeadService {

    @Override
    public LegoPage<CrmLeadInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<CrmLead> leads = dao.findPageBy(buildCondition(vo));
        return assembler.create(leads);
    }

    @Override
    public CrmLeadInfo findBy(String code) {
        CrmLead lead = dao.findByCode(code);
        return assembler.create(lead);
    }

    @Override
    public TypeInfo findSimpleBy(String code) {
        CrmLead lead = dao.findByCode(code);
        return assembler.createTypeInfo(lead);
    }

    @Override
    public List<CrmLeadInfo> findBy(GenericSearchVO vo) {
        List<CrmLead> leads = dao.findBy(buildCondition(vo));
        return assembler.create(leads);
    }

    @Override
    public List<CrmLeadInfo> findBy(List<String> codes) {
        List<CrmLead> leads = dao.findByCodes(codes);
        return assembler.create(leads);
    }

    @Override
    public void update(String operatorCode, CrmLeadModifyVO vo) {
        new ModifyCrmLeadAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, CrmLeadCreateVO vo) {
        new AddCrmLeadAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteCrmLeadAction(operatorCode, code).run();
        }
    }

    @Override
    public void updateCheckStatus(String code, String checkStatus) {
        CrmLead lead = dao.findByCode(code);
        lead.updateCheckStatus(checkStatus);
        dao.save(lead);
    }
}
