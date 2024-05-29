package com.lego.doc.action;

import com.lego.core.action.DeleteAction;
import com.lego.core.exception.BusinessException;
import com.lego.doc.dao.IDocPageDao;
import com.lego.doc.entity.DocPage;

public class DeleteDocPageAction extends DeleteAction<DocPage, IDocPageDao> {

    public DeleteDocPageAction(String operatorCode, String code) {
        super("doc_book", operatorCode, code);
    }

    @Override
    protected void preprocess() {
        BusinessException.check(targetEntity.getBook().checkPermission(operatorCode), "用户无[{0}]文章的删除权限，文章删除失败！", targetEntity.getName());
    }
}
