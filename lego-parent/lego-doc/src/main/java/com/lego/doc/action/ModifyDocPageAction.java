package com.lego.doc.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocPageDao;
import com.lego.doc.entity.DocPage;
import com.lego.doc.vo.DocPageModifyVO;

public class ModifyDocPageAction extends ModifyAction<DocPage, IDocPageDao> {

    private DocPageModifyVO vo;

    public ModifyDocPageAction(String operatorCode, DocPageModifyVO vo) {
        super("doc_book", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getContent()), "内容不能为空，知识库文章修改失败！");
        BusinessException.check(targetEntity.checkPermission(operatorCode), "用户无[{0}]修改权限，知识库文章修改失败！", targetEntity.getName());
    }

    @Override
    protected void doModify(DocPage entity) {
        entity.setContent(vo.getContent());
    }

}
