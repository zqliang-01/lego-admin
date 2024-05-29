package com.lego.doc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.doc.action.AddDocCollectAction;
import com.lego.doc.action.DeleteDocCollectAction;
import com.lego.doc.assembler.DocCollectAssembler;
import com.lego.doc.dao.IDocCollectDao;
import com.lego.doc.dto.DocCollectInfo;
import com.lego.doc.entity.DocCollect;
import com.lego.doc.service.IDocCollectService;
import com.lego.doc.vo.DocCollectSearchVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocCollectService extends BusService<IDocCollectDao, DocCollectAssembler> implements IDocCollectService {

    @Override
    public LegoPage<DocCollectInfo> findPageBy(String operatorCode, DocCollectSearchVO vo) {
        LegoPage<DocCollect> collects = dao.findBy(operatorCode, vo);
        return assembler.create(collects);
    }

    @Override
    public String add(String operatorCode, String nodeCode) {
        AddDocCollectAction addDocCollectAction = new AddDocCollectAction(operatorCode, nodeCode);
        addDocCollectAction.run();
        return addDocCollectAction.getCode();
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteDocCollectAction(operatorCode, code).run();
    }

    @Override
    public String findCodeBy(String operatorCode, String node) {
        List<DocCollect> collects = dao.findBy(node, operatorCode);
        if (CollectionUtil.isNotEmpty(collects)) {
            return collects.get(0).getCode();
        }
        return null;
    }

}
