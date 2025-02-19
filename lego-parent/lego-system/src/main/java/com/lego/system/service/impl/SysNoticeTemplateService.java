package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.system.action.AddSysNoticeTemplateAction;
import com.lego.system.action.ModifySysNoticeTemplateAction;
import com.lego.system.action.PublishSysNoticeTemplateAction;
import com.lego.system.assembler.SysNoticeTemplateAssembler;
import com.lego.system.dao.ISysNoticeTemplateDao;
import com.lego.system.dto.SysNoticeTemplateInfo;
import com.lego.system.service.ISysNoticeTemplateService;
import com.lego.system.vo.SysNoticeTemplateCreateVO;
import com.lego.system.vo.SysNoticeTemplateModifyVO;
import com.lego.system.vo.SysNoticeTemplateSearchVO;
import org.springframework.stereotype.Service;

@Service
public class SysNoticeTemplateService extends BaseService<ISysNoticeTemplateDao, SysNoticeTemplateAssembler> implements ISysNoticeTemplateService {

    @Override
    public LegoPage<SysNoticeTemplateInfo> findBy(String operatorCode, SysNoticeTemplateSearchVO vo) {
        GenericConditionVO conditionVO = GenericConditionVO.create(vo);
        if (StringUtil.isNotBlank(vo.getName())) {
            conditionVO.addItem(GenericConditionItemVO.createLike("name", "%" + vo.getName() + "%"));
        }
        if (vo.getPublished() != null) {
            conditionVO.addItem(GenericConditionItemVO.createEqual("published", vo.getPublished()));
        }
        return assembler.create(dao.findPageBy(conditionVO));
    }

    @Override
    public void add(String operatorCode, SysNoticeTemplateCreateVO vo) {
        new AddSysNoticeTemplateAction(operatorCode, vo).run();
    }

    @Override
    public void modify(String operatorCode, SysNoticeTemplateModifyVO vo) {
        new ModifySysNoticeTemplateAction(operatorCode, vo).run();
    }

    @Override
    public void publish(String operatorCode, String code) {
        new PublishSysNoticeTemplateAction(operatorCode, code).run();
    }
}
