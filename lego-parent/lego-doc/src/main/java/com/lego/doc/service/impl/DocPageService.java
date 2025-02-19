package com.lego.doc.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.exception.BusinessException;
import com.lego.doc.action.ModifyDocPageAction;
import com.lego.doc.assembler.DocPageAssembler;
import com.lego.doc.dao.IDocPageDao;
import com.lego.doc.dto.DocPageInfo;
import com.lego.doc.entity.DocPage;
import com.lego.doc.service.IDocPageService;
import com.lego.doc.vo.DocPageModifyVO;
import org.springframework.stereotype.Service;

@Service
public class DocPageService extends BaseService<IDocPageDao, DocPageAssembler> implements IDocPageService {

    @Override
    public DocPageInfo findBy(String operatorCode, String code) {
        DocPage page = dao.findByCode(code);
        boolean permission = page.checkPermission(operatorCode);
        if (!page.getBook().isOpen()) {
            BusinessException.check(permission, "内容[{0}]所属知识库未公开，用户无查看权限！", page.getName());
        }

        DocPageInfo docPageInfo = assembler.create(page);
        docPageInfo.setEditable(permission);
        return docPageInfo;
    }

    @Override
    public void update(String operatorCode, DocPageModifyVO vo) {
        new ModifyDocPageAction(operatorCode, vo).run();
    }

}
