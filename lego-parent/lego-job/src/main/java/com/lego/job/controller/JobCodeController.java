package com.lego.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.exception.BusinessException;
import com.lego.core.job.glue.GlueTypeEnum;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.core.model.XxlJobLogGlue;
import com.lego.job.dto.JobCodeInfo;
import com.lego.job.mapper.XxlJobInfoMapper;
import com.lego.job.mapper.XxlJobLogGlueMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/back-end/job-code")
public class JobCodeController extends BaseController {

    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    private XxlJobLogGlueMapper xxlJobLogGlueMapper;

    @GetMapping("/index/{jobId}")
    @SaCheckPermission("job_task_update")
    public JsonResponse<JobCodeInfo> index(@PathVariable int jobId) {
        XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(jobId);
        List<XxlJobLogGlue> jobLogGlues = xxlJobLogGlueMapper.findByJobId(jobId);

        BusinessException.check(jobInfo != null, "任务ID非法");
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
            throw new BusinessException("该任务非GLUE模式");
        }

        JobCodeInfo info = new JobCodeInfo();
        info.setJobInfo(jobInfo);
        info.setJobLogGlues(jobLogGlues);
        info.setGlueTypes(GlueTypeEnum.getTypeInfo());
        return JsonResponse.success(info);
    }

    @PostMapping("/save")
    @SaCheckPermission("job_task_update")
    public JsonResponse<Object> save(int id, String glueSource, String glueRemark) {
        BusinessException.check(StringUtil.isNotBlank(glueRemark), "请输入源码备注");
        BusinessException.check(glueRemark.length() >= 4 && glueRemark.length() < 100, "源码备注长度限制为4~100");
        XxlJobInfo exists_jobInfo = xxlJobInfoMapper.loadById(id);
        BusinessException.check(exists_jobInfo != null, "任务ID非法");

        // update new code
        exists_jobInfo.setGlueSource(glueSource);
        exists_jobInfo.setGlueRemark(glueRemark);
        exists_jobInfo.setGlueUpdatetime(new Date());

        exists_jobInfo.setUpdateTime(new Date());
        xxlJobInfoMapper.update(exists_jobInfo);

        // log old code
        XxlJobLogGlue xxlJobLogGlue = new XxlJobLogGlue();
        xxlJobLogGlue.setJobId(exists_jobInfo.getId());
        xxlJobLogGlue.setGlueType(exists_jobInfo.getGlueType());
        xxlJobLogGlue.setGlueSource(glueSource);
        xxlJobLogGlue.setGlueRemark(glueRemark);

        xxlJobLogGlue.setAddTime(new Date());
        xxlJobLogGlue.setUpdateTime(new Date());
        xxlJobLogGlueMapper.save(xxlJobLogGlue);

        // remove code backup more than 30
        xxlJobLogGlueMapper.removeOld(exists_jobInfo.getId(), 30);

        return JsonResponse.success();
    }

}
