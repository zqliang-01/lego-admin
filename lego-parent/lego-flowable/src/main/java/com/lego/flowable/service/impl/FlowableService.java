package com.lego.flowable.service.impl;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.PageVO;
import org.flowable.common.engine.api.query.Query;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.List;

public class FlowableService<A extends BaseAssembler> {

    @Resource
    protected TaskService taskService;
    @Resource
    protected RuntimeService runtimeService;
    @Resource
    protected IdentityService identityService;
    @Resource
    protected RepositoryService repositoryService;
    @Resource
    protected HistoryService historyService;
    @Qualifier("processEngine")
    @Resource
    protected ProcessEngine processEngine;
    @Autowired
    protected A assembler;

    protected <T> LegoPage<T> createPage(Query query, PageVO vo, Class<T> clazz) {
        long pageTotal = query.count();
        if (pageTotal <= 0) {
            return new LegoPage<T>(vo, 0);
        }
        int offset = vo.getPageSize() * (vo.getPageIndex() - 1);
        List<T> results = query.listPage(offset, vo.getPageSize());
        return new LegoPage<T>(results, vo, pageTotal);
    }

}
