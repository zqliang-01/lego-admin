package com.lego.doc.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocNodeDao;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocFile;
import com.lego.doc.entity.DocNode;
import com.lego.doc.entity.DocNodeFile;
import com.lego.doc.entity.DocNodeFolder;
import com.lego.doc.entity.DocNodePage;
import com.lego.doc.entity.DocPage;
import com.lego.doc.entity.simpletype.DocNodeType;
import com.lego.doc.vo.DocNodeCreateVO;
import com.lego.doc.vo.DocNodeTypeCode;

public class AddDocNodeAction extends AddAction<DocNode, IDocNodeDao> {

    private DocNodeCreateVO vo;

    public AddDocNodeAction(String operatorCode, DocNodeCreateVO vo) {
        super("doc_book", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getBook()), "所属知识库不能为空，创建失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getType()), "类型不能为空，创建失败！");
        if (!DocNodeTypeCode.FILE.equals(vo.getType())) {
            BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，创建失败！");
        }
    }

    @Override
    protected DocNode createTargetEntity() {
        DocBook book = findByCode(DocBook.class, vo.getBook());
        BusinessException.check(book.checkPermission(operatorCode), "用户无[{0}]操作权限，创建失败！", EntityUtil.getName(book));

        DocNode parentNode = findByUnsureCode(DocNode.class, vo.getParentCode());
        if (parentNode != null) {
            BusinessException.check(parentNode.isFolder(), "上级节点[{0}]不是目录节点，节点创建失败！", EntityUtil.getName(parentNode));
        }
        DocNodeType type = findByCode(DocNodeType.class, vo.getType());
        if (DocNodeTypeCode.FILE.equals(vo.getType())) {
            DocFile file = findByCode(DocFile.class, vo.getFileCode());
            DocNode node = new DocNodeFile(operatorCode, file, book, type);
            node.setParent(parentNode);
            node.setDescription(vo.getDescription());
            return node;
        }
        if (DocNodeTypeCode.PAGE.equals(vo.getType())) {
            AddDocPageAction addPageAction = new AddDocPageAction(operatorCode, vo.getName(), book);
            addPageAction.run();
            DocPage page = addPageAction.getPage();
            DocNode node = new DocNodePage(operatorCode, page, book, type);
            node.setParent(parentNode);
            node.setDescription(vo.getDescription());
            return node;
        }
        BusinessException.check(DocNodeTypeCode.FOLDER.equals(vo.getType()), "未知的节点类型[{0}]，节点创建失败！", vo.getType());
        DocNode node = new DocNodeFolder(vo.getName(), operatorCode, book, type);
        node.setParent(parentNode);
        node.setDescription(vo.getDescription());
        return node;
    }

}
