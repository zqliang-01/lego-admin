package com.lego.crm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.action.AddCrmContractAction;
import com.lego.crm.action.DeleteCrmContractAction;
import com.lego.crm.action.ModifyCrmContractAction;
import com.lego.crm.assembler.CrmContractAssembler;
import com.lego.crm.dao.ICrmContractDao;
import com.lego.crm.dto.CrmContractInfo;
import com.lego.crm.entity.CrmContract;
import com.lego.crm.service.ICrmContractService;
import com.lego.crm.vo.CrmContractCreateVO;
import com.lego.crm.vo.CrmContractModifyVO;

@Service
public class CrmContractService extends BusService<ICrmContractDao, CrmContractAssembler> implements ICrmContractService {

    @Override
    public LegoPage<CrmContractInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<CrmContract> contracts = dao.findPageBy(buildCondition(vo));
        return assembler.create(contracts);
    }

    @Override
    public CrmContractInfo findBy(String code) {
        CrmContract contract = dao.findByCode(code);
        return assembler.create(contract);
    }

    @Override
    public TypeInfo findSimpleBy(String code) {
        CrmContract contract = dao.findByCode(code);
        return assembler.createTypeInfo(contract);
    }

    @Override
    public List<CrmContractInfo> findBy(GenericSearchVO vo) {
        List<CrmContract> contracts = dao.findBy(buildCondition(vo));
        return assembler.create(contracts);
    }

    @Override
    public List<CrmContractInfo> findBy(List<String> codes) {
        List<CrmContract> contracts = dao.findByCodes(codes);
        return assembler.create(contracts);
    }

    @Override
    public void update(String operatorCode, CrmContractModifyVO vo) {
        new ModifyCrmContractAction(operatorCode, vo).run();
    }

    @Override
    public String add(String operatorCode, CrmContractCreateVO vo) {
        AddCrmContractAction addCrmContractAction = new AddCrmContractAction(operatorCode, vo);
        addCrmContractAction.run();
        return addCrmContractAction.getEntityCode();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteCrmContractAction(operatorCode, code).run();
        }
    }

    @Override
    public void updateCheckStatus(String code, String checkStatus) {
        CrmContract contract = dao.findByCode(code);
        contract.updateCheckStatus(checkStatus);
        dao.save(contract);
    }
}
