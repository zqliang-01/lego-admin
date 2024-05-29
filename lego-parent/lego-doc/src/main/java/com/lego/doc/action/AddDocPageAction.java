package com.lego.doc.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocPageDao;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocPage;

public class AddDocPageAction extends AddAction<DocPage, IDocPageDao> {

    private String name;
    private DocBook book;

    public AddDocPageAction(String operatorCode, String name, DocBook book) {
        super("doc_book", operatorCode);
        this.name = name;
        this.book = book;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(name), "名称不能为空，知识库文章新增失败！");
        BusinessException.check(book.checkPermission(operatorCode), "用户无知识库[{0}]操作权限，文章创建失败！", book.getName());
    }

    @Override
    protected DocPage createTargetEntity() {
        DocPage entity = new DocPage(name, operatorCode);
        entity.setBook(book);
        return entity;
    }

    public DocPage getPage() {
        return this.targetEntity;
    }

    @Override
    protected void createLog() {
    }
}
