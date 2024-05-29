package com.lego.doc.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import com.lego.doc.dao.IDocNodeDao;
import com.lego.doc.entity.DocNode;
import com.lego.doc.entity.DocNodeFile;
import com.lego.doc.entity.DocNodePage;
import com.lego.doc.vo.DocNodeModifyVO;

public class ModifyDocNodeAction extends ModifyAction<DocNode, IDocNodeDao> {

    private DocNodeModifyVO vo;

    public ModifyDocNodeAction(String operatorCode, DocNodeModifyVO vo) {
        super("doc_book", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，修改失败！");
        BusinessException.check(targetEntity.checkPermission(operatorCode), "用户无修改[{0}]的权限，修改失败！", targetEntity.getName());
    }

    @Override
    protected void doModify(DocNode entity) {
        entity.setName(vo.getName());
        entity.setUpdateTime(DateUtil.currentDateTime());
        if (entity instanceof DocNodePage) {
            DocNodePage nodePage = (DocNodePage) entity;
            nodePage.getPage().setName(vo.getName());
        }
        if (entity instanceof DocNodeFile) {
            DocNodeFile nodeFile = (DocNodeFile) entity;
            nodeFile.getFile().setName(vo.getName());
        }
    }

}
