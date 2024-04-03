package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.system.action.AddSysPrintTemplateAction;
import com.lego.system.action.DeleteSysPrintTemplateAction;
import com.lego.system.action.DesignSysPrintTemplateAction;
import com.lego.system.action.ModifySysPrintTemplateAction;
import com.lego.system.assembler.SysPrintTemplateAssembler;
import com.lego.system.dao.ISysPrintTemplateDao;
import com.lego.system.dto.SysPrintTemplateInfo;
import com.lego.system.entity.SysPrintTemplate;
import com.lego.system.service.ISysPrintTemplateService;
import com.lego.system.util.PrintUtil;
import com.lego.system.vo.SysPrintTemplateCreateVO;
import com.lego.system.vo.SysPrintTemplateModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysPrintTemplateService extends BusService<ISysPrintTemplateDao, SysPrintTemplateAssembler> implements ISysPrintTemplateService {

    @Override
    public LegoPage<SysPrintTemplateInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<SysPrintTemplate> printTemplates = dao.findPageBy(buildCondition(vo));
        return assembler.create(printTemplates);
    }

    @Override
    public List<TypeInfo> findSimpleBy(String formCode) {
        List<SysPrintTemplate> templates = dao.findBy(formCode);
        return assembler.createTypeInfo(templates);
    }

    @Override
    public SysPrintTemplateInfo findBy(String code) {
        SysPrintTemplate printTemplate = dao.findByCode(code);
        return assembler.create(printTemplate);
    }

    @Override
    public void update(String operatorCode, SysPrintTemplateModifyVO vo) {
        new ModifySysPrintTemplateAction(operatorCode, vo).run();
    }

    @Override
    public void design(String operatorCode, SysPrintTemplateModifyVO vo) {
        new DesignSysPrintTemplateAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, SysPrintTemplateCreateVO vo) {
        new AddSysPrintTemplateAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteSysPrintTemplateAction(operatorCode, code).run();
    }

    @Override
    public String print(String code, Map<String, String> params) {
        SysPrintTemplate template = dao.findByCode(code);
        return PrintUtil.buildPrintContent(template.getContent(), params);
    }

    @Override
    public String preview(String content, String fileType) {
        return PrintUtil.buildTempFile(content, fileType);
    }

}
