package com.lego.doc.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.DateUtil;
import com.lego.doc.dao.IDocNodeDao;
import com.lego.doc.entity.DocNode;

public class ModifyDocNodeStatusAction extends ModifyAction<DocNode, IDocNodeDao> {

    private boolean enable;

    public ModifyDocNodeStatusAction(String operatorCode, String code, boolean enable) {
        super("doc_book", operatorCode, code);
        this.enable = enable;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(targetEntity.checkPermission(operatorCode), "用户无[{0}]删除权限！", targetEntity.getName());
    }

    @Override
    protected void doModify(DocNode entity) {
        entity.setEnable(enable);
        entity.setUpdateTime(DateUtil.currentDateTime());
    }
}
