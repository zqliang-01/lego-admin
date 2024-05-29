package com.lego.doc.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.core.vo.GenericSearchVO;
import com.lego.doc.action.AddDocBookAction;
import com.lego.doc.action.DeleteDocBookAction;
import com.lego.doc.action.ModifyDocBookAction;
import com.lego.doc.action.ModifyDocBookStatusAction;
import com.lego.doc.assembler.DocBookAssembler;
import com.lego.doc.dao.IDocBookDao;
import com.lego.doc.dto.DocBookInfo;
import com.lego.doc.entity.DocBook;
import com.lego.doc.service.IDocBookService;
import com.lego.doc.vo.DocBookCreateVO;
import com.lego.doc.vo.DocBookModifyVO;
import org.springframework.stereotype.Service;

@Service
public class DocBookService extends BusService<IDocBookDao, DocBookAssembler> implements IDocBookService {

    @Override
    public LegoPage<DocBookInfo> findPageBy(String operatorCode, GenericSearchVO vo) {
        GenericConditionVO condition = buildCondition(vo);
        condition.addItem(GenericConditionItemVO.createEqual("enable", true));
        condition.addItem(GenericConditionItemVO.createEqual("creatorCode", operatorCode));
        condition.setOrderType("sn, createTime");
        LegoPage<DocBook> books = dao.findPageBy(condition);
        return assembler.create(books);
    }

    @Override
    public LegoPage<DocBookInfo> findDisablePageBy(String operatorCode, GenericSearchVO vo) {
        GenericConditionVO condition = buildCondition(vo);
        condition.addItem(GenericConditionItemVO.createEqual("enable", false));
        condition.addItem(GenericConditionItemVO.createEqual("creatorCode", operatorCode));
        condition.setOrderType("sn, createTime");
        LegoPage<DocBook> books = dao.findPageBy(condition);
        return assembler.create(books);
    }

    @Override
    public LegoPage<DocBookInfo> findPublicPageBy(String operatorCode, GenericSearchVO vo) {
        GenericConditionVO condition = buildCondition(vo);
        condition.addItem(GenericConditionItemVO.createEqual("open", true));
        condition.addItem(GenericConditionItemVO.createEqual("enable", true));
        condition.setOrderType("sn, createTime");
        LegoPage<DocBook> books = dao.findPageBy(condition);
        return assembler.create(books);
    }

    @Override
    public DocBookInfo findBy(String operatorCode, String code) {
        DocBook book = dao.findByCode(code);
        boolean permission = book.checkPermission(operatorCode);
        if (!book.isOpen()) {
            BusinessException.check(permission, "用户无查看知识库[{0}]权限！", book.getName());
        }
        DocBookInfo docBookInfo = assembler.create(book);
        docBookInfo.setEditable(permission);
        return docBookInfo;
    }

    @Override
    public void update(String operatorCode, DocBookModifyVO vo) {
        new ModifyDocBookAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, DocBookCreateVO vo) {
        new AddDocBookAction(operatorCode, vo).run();
    }

    @Override
    public void disable(String operatorCode, String code) {
        new ModifyDocBookStatusAction(operatorCode, code, false).run();
    }

    @Override
    public void enable(String operatorCode, String code) {
        new ModifyDocBookStatusAction(operatorCode, code, true).run();
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteDocBookAction(operatorCode, code).run();
    }

}
