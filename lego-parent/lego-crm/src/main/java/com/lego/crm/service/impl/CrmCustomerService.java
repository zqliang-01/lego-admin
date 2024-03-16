package com.lego.crm.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.crm.action.AddCrmCustomerAction;
import com.lego.crm.action.DeleteCrmCustomerAction;
import com.lego.crm.action.ModifyCrmCustomerAction;
import com.lego.crm.assembler.CrmCustomerAssembler;
import com.lego.crm.dao.ICrmCustomerDao;
import com.lego.crm.dto.CrmCustomerInfo;
import com.lego.crm.entity.CrmCustomer;
import com.lego.crm.service.ICrmCustomerService;
import com.lego.crm.vo.CrmCustomerCreateVO;
import com.lego.crm.vo.CrmCustomerModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmCustomerService extends BusService<ICrmCustomerDao, CrmCustomerAssembler> implements ICrmCustomerService {

    @Override
    public LegoPage<CrmCustomerInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<CrmCustomer> customers = dao.findPageBy(buildCondition(vo));
        return assembler.create(customers);
    }

    @Override
    public CrmCustomerInfo findBy(String code) {
        CrmCustomer customer = dao.findByCode(code);
        return assembler.create(customer);
    }

    @Override
    public TypeInfo findSimpleBy(String code) {
        CrmCustomer customer = dao.findByCode(code);
        return assembler.createTypeInfo(customer);
    }

    @Override
    public List<CrmCustomerInfo> findBy(GenericSearchVO vo) {
        List<CrmCustomer> customers = dao.findBy(buildCondition(vo));
        return assembler.create(customers);
    }

    @Override
    public List<CrmCustomerInfo> findBy(List<String> codes) {
        List<CrmCustomer> customers = dao.findByCodes(codes);
        return assembler.create(customers);
    }

    @Override
    public void update(String operatorCode, CrmCustomerModifyVO vo) {
        new ModifyCrmCustomerAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, CrmCustomerCreateVO vo) {
        new AddCrmCustomerAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteCrmCustomerAction(operatorCode, code).run();
        }
    }

    @Override
    public void updateCheckStatus(String code, String checkStatus) {
        CrmCustomer customer = dao.findByCode(code);
        customer.updateCheckStatus(checkStatus);
        dao.save(customer);
    }
}
