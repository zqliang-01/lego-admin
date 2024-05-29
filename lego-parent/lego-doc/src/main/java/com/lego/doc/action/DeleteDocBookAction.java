package com.lego.doc.action;

import com.lego.core.action.DeleteAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.web.LegoBeanFactory;
import com.lego.core.web.upload.FileHandler;
import com.lego.doc.dao.IDocBookDao;
import com.lego.doc.dao.IDocCollectDao;
import com.lego.doc.dao.IDocFileDao;
import com.lego.doc.dao.IDocNodeDao;
import com.lego.doc.dao.IDocPageDao;
import com.lego.doc.entity.DocBook;
import com.lego.doc.entity.DocCollect;
import com.lego.doc.entity.DocFile;
import com.lego.doc.entity.DocNode;
import com.lego.doc.entity.DocNodeFile;
import com.lego.doc.entity.DocNodePage;
import com.lego.doc.entity.DocPage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DeleteDocBookAction extends DeleteAction<DocBook, IDocBookDao> {

    private List<DocFile> nodeFiles = new ArrayList<>();
    private IDocNodeDao nodeDao = getDao(IDocNodeDao.class);
    private IDocPageDao pageDao = getDao(IDocPageDao.class);
    private IDocFileDao fileDao = getDao(IDocFileDao.class);
    private IDocCollectDao collectDao = getDao(IDocCollectDao.class);
    private FileHandler fileHandler = LegoBeanFactory.getBean(FileHandler.class);

    public DeleteDocBookAction(String operatorCode, String code) {
        super("doc_book", operatorCode, code);
    }

    @Override
    protected void preprocess() {
        BusinessException.check(targetEntity.checkPermission(operatorCode), "用户无该知识库[{0}]的删除权限，知识库删除失败！", targetEntity.getName());

        List<DocNode> nodes = nodeDao.findBy(targetEntity);
        List<DocCollect> collects = collectDao.findBy(nodes);
        collectDao.deleteAllInBatch(collects);

        List<DocPage> pages = getNodePage(nodes);
        pageDao.deleteAllInBatch(pages);

        List<DocFile> files = getNodeFile(nodes);
        fileDao.deleteAllInBatch(files);

        nodeDao.deleteAllInBatch(nodes);
    }

    private List<DocFile> getNodeFile(List<DocNode> nodes) {
        List<DocFile> files = new ArrayList<>();
        for (DocNode node : nodes) {
            if (node instanceof DocNodeFile) {
                DocNodeFile nodeFile = (DocNodeFile) node;
                DocFile file = nodeFile.getFile();
                files.add(file);
                nodeFiles.add(file);
            }
        }
        return files;
    }

    private List<DocPage> getNodePage(List<DocNode> nodes) {
        List<DocPage> pages = new ArrayList<>();
        for (DocNode node : nodes) {
            if (node instanceof DocNodePage) {
                DocNodePage nodePage = (DocNodePage) node;
                pages.add(nodePage.getPage());
            }
        }
        return pages;
    }

    @Override
    protected void postprocess() {
        for (DocFile nodeFile : nodeFiles) {
            try {
                fileHandler.delete(nodeFile.getPath());
            } catch (Exception e) {
                log.error("删除图书附件[" + nodeFile.getName() + "]失败！", e);
            }
        }
    }
}
