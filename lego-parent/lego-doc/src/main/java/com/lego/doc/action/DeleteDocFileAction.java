package com.lego.doc.action;

import com.lego.core.action.DeleteAction;
import com.lego.doc.dao.IDocFileDao;
import com.lego.doc.entity.DocFile;

public class DeleteDocFileAction extends DeleteAction<DocFile, IDocFileDao> {

    public DeleteDocFileAction(String operatorCode, String code) {
        super("doc_book", operatorCode, code);
    }
}
