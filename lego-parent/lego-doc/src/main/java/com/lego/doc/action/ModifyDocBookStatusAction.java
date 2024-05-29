package com.lego.doc.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.DateUtil;
import com.lego.doc.dao.IDocBookDao;
import com.lego.doc.entity.DocBook;

public class ModifyDocBookStatusAction extends ModifyAction<DocBook, IDocBookDao> {

    private boolean enable;

    public ModifyDocBookStatusAction(String operatorCode, String code, boolean enable) {
        super("doc_book", operatorCode, code);
        this.enable = enable;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(targetEntity.checkPermission(operatorCode), "用户无该知识库[{0}]的更新权限，知识库更新失败！", targetEntity.getName());
    }

    @Override
    protected void doModify(DocBook entity) {
        entity.setEnable(enable);
        entity.setUpdateTime(DateUtil.currentDateTime());
    }
}
