package com.lego.doc.action;

import com.lego.core.action.DeleteAction;
import com.lego.core.exception.BusinessException;
import com.lego.doc.dao.IDocCollectDao;
import com.lego.doc.entity.DocCollect;

public class DeleteDocCollectAction extends DeleteAction<DocCollect, IDocCollectDao> {

    public DeleteDocCollectAction(String operatorCode, String code) {
        super("doc_book", operatorCode, code);
    }

    @Override
    protected void preprocess() {
        BusinessException.check(targetEntity.getCreatorCode().equals(operatorCode), "不能取消别人的收藏，取消收藏失败！");
    }
}
