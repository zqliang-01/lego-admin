package com.lego.report.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.report.action.AddReportDesignAction;
import com.lego.report.action.DeleteReportDesignAction;
import com.lego.report.action.ModifyReportDesignAction;
import com.lego.report.action.ModifyReportDesignSortAction;
import com.lego.report.assembler.ReportDesignAssembler;
import com.lego.report.dao.IReportDesignDao;
import com.lego.report.dto.ReportDesignInfo;
import com.lego.report.entity.ReportDesign;
import com.lego.report.service.IReportDesignService;
import com.lego.report.vo.ReportDesignCreateVO;
import com.lego.report.vo.ReportDesignModifyVO;
import com.lego.report.vo.ReportDesignSortModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportDesignService extends BaseService<IReportDesignDao, ReportDesignAssembler> implements IReportDesignService {

    @Override
    public List<ReportDesignInfo> findAll() {
        List<ReportDesign> results = dao.findAll();
        return assembler.create(results);
    }

    @Override
    public List<ReportDesignInfo> findValid(String loginCode) {
        List<ReportDesign> results = dao.findValid(loginCode);
        return assembler.create(results);
    }

    @Override
    public ReportDesignInfo findByCode(String code) {
        ReportDesign design = dao.findByCode(code);
        return assembler.create(design);
    }

    @Override
    public void add(String loginCode, ReportDesignCreateVO vo) {
        new AddReportDesignAction(loginCode, vo).run();
    }

    @Override
    public void update(String loginCode, ReportDesignModifyVO vo) {
        new ModifyReportDesignAction(loginCode, vo).run();
    }

    @Override
    public void updateSort(String loginCode, ReportDesignSortModifyVO vo) {
        new ModifyReportDesignSortAction(loginCode, vo).run();
    }

    @Override
    public void delete(String loginCode, String code) {
        new DeleteReportDesignAction(loginCode, code).run();
    }
}
