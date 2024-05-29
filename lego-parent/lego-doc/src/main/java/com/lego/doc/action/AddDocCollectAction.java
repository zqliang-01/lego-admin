package com.lego.doc.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocCollectDao;
import com.lego.doc.entity.DocCollect;
import com.lego.doc.entity.DocNode;

import java.util.List;

public class AddDocCollectAction extends AddAction<DocCollect, IDocCollectDao> {

    private String nodeCode;

    public AddDocCollectAction(String operatorCode, String nodeCode) {
        super("doc_book", operatorCode);
        this.nodeCode = nodeCode;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(nodeCode), "收藏内容不能为空，收藏新增失败！");
        List<DocCollect> collects = entityDao.findBy(nodeCode, operatorCode);
        BusinessException.check(collects.isEmpty(), "您已经收藏过该内容，无需重复收藏！");
    }

    @Override
    protected DocCollect createTargetEntity() {
        DocNode node = findByCode(DocNode.class, nodeCode);
        if (!node.getBook().isOpen()) {
            BusinessException.check(node.checkPermission(operatorCode), "用户无收藏[{0}]权限！", node.getName());
        }
        return new DocCollect(operatorCode, node);
    }

    public String getCode() {
        return this.targetEntity.getCode();
    }
}
