package com.lego.doc.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocBookDao;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocFile;
import com.lego.doc.vo.DocBookCreateVO;

public class AddDocBookAction extends AddAction<DocBook, IDocBookDao> {

    private DocBookCreateVO vo;

    public AddDocBookAction(String operatorCode, DocBookCreateVO vo) {
        super("doc_book", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，知识库新增失败！");
    }

    @Override
    protected DocBook createTargetEntity() {
        DocBook entity = new DocBook(vo.getName(), operatorCode);
        entity.setOpen(vo.isOpen());
        entity.setDescription(vo.getDescription());
        entity.setCover(findByUnsureCode(DocFile.class, vo.getCover()));
        return entity;
    }

}
