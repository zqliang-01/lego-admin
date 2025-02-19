package com.lego.doc.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.doc.action.AddDocNodeAction;
import com.lego.doc.action.ModifyDocNodeAction;
import com.lego.doc.action.ModifyDocNodeStatusAction;
import com.lego.doc.assembler.DocNodeAssembler;
import com.lego.doc.dao.IDocBookDao;
import com.lego.doc.dao.IDocNodeDao;
import com.lego.doc.dto.DocNodeDetailInfo;
import com.lego.doc.dto.DocNodeInfo;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocNode;
import com.lego.doc.service.IDocNodeService;
import com.lego.doc.vo.DocNodeCreateVO;
import com.lego.doc.vo.DocNodeModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocNodeService extends BaseService<IDocNodeDao, DocNodeAssembler> implements IDocNodeService {

    @Autowired
    private IDocBookDao bookDao;

    @Override
    public void update(String operatorCode, DocNodeModifyVO vo) {
        new ModifyDocNodeAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, DocNodeCreateVO vo) {
        new AddDocNodeAction(operatorCode, vo).run();
    }

    @Override
    public List<DocNodeInfo> findBy(String bookCode, String type, String loginCode) {
        DocBook book = bookDao.findByCode(bookCode);
        BusinessException.check(book.isEnable(), "知识库[{0}]已删除！", book.getName());
        if (!book.isOpen()) {
            BusinessException.check(book.checkPermission(loginCode), "用户无操作权限");
        }
        List<DocNode> nodes = dao.findBy(book, type);
        return assembler.createTree(nodes).stream()
            .filter(node -> nodes.stream().anyMatch(n -> StringUtil.isBlank(node.getParentCode()) || n.getCode().equals(node.getParentCode())))
            .collect(Collectors.toList());
    }

    @Override
    public List<DocNodeDetailInfo> findChildrenBy(String code, String type, String loginCode) {
        DocNode node = dao.findByCode(code);
        DocBook book = node.getBook();
        BusinessException.check(book.isEnable(), "知识库[{0}]已删除！", book.getName());
        if (!book.isOpen()) {
            BusinessException.check(book.checkPermission(loginCode), "用户无操作权限");
        }
        List<DocNode> nodes = dao.findChildrenBy(book, node, type);
        return assembler.createDetail(nodes);
    }

    @Override
    public void disable(String loginCode, String code) {
        new ModifyDocNodeStatusAction(loginCode, code, false).run();
    }

    @Override
    public DocNodeDetailInfo findBy(String operatorCode, String code) {
        DocNode node = dao.findByCode(code);
        boolean permission = node.checkPermission(operatorCode);
        if (!node.getBook().isOpen()) {
            BusinessException.check(permission, "内容[{0}]所属知识库未公开，用户无查看权限！", node.getName());
        }
        DocNodeDetailInfo detail = assembler.createDetail(node);
        detail.setEditable(permission);
        return detail;
    }

}
