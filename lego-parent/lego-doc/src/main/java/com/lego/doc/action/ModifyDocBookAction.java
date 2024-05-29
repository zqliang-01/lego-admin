package com.lego.doc.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocBookDao;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocFile;
import com.lego.doc.vo.DocBookModifyVO;

public class ModifyDocBookAction extends ModifyAction<DocBook, IDocBookDao> {

    private DocBookModifyVO vo;

    public ModifyDocBookAction(String operatorCode, DocBookModifyVO vo) {
        super("doc_book", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，知识库修改失败！");
        BusinessException.check(targetEntity.checkPermission(operatorCode), "用户无知识库[{0}]修改权限，知识库修改失败！", targetEntity.getName());
    }

    @Override
    protected void doModify(DocBook entity) {
        entity.setName(vo.getName());
        entity.setOpen(vo.isOpen());
        entity.setDescription(vo.getDescription());
        entity.setCover(findByUnsureCode(DocFile.class, vo.getCover()));
        entity.setUpdateTime(DateUtil.currentDateTime());
    }

}
