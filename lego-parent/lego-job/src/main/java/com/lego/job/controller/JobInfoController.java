package com.lego.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.job.enums.ExecutorBlockStrategyEnum;
import com.lego.core.job.glue.GlueTypeEnum;
import com.lego.core.job.util.XxlDateUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.job.core.model.XxlJobGroup;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.core.route.ExecutorRouteStrategyEnum;
import com.lego.job.core.scheduler.MisfireStrategyEnum;
import com.lego.job.core.scheduler.ScheduleTypeEnum;
import com.lego.job.core.thread.JobScheduleHelper;
import com.lego.job.dto.JobConfigInfo;
import com.lego.job.mapper.XxlJobGroupMapper;
import com.lego.job.service.XxlJobService;
import com.lego.job.vo.JobConfigSearchVO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/back-end/job-task")
public class JobInfoController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);

    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;
    @Resource
    private XxlJobService xxlJobService;

    @GetMapping("/index")
    @SaCheckPermission("job_task_read")
    public JsonResponse<JobConfigInfo> index(@RequestParam(required = false, defaultValue = "-1") int jobGroup) {

        // 执行器列表
        List<XxlJobGroup> jobGroupList = xxlJobGroupMapper.findAll();

        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new BusinessException("不存在有效执行器，请联系管理员");
        }

        JobConfigInfo info = new JobConfigInfo();
        info.setExecutorBlockStrategies(ExecutorBlockStrategyEnum.getTypeInfo());
        info.setExecutorRouteStrategies(ExecutorRouteStrategyEnum.getTypeInfo());
        info.setGlueTypes(GlueTypeEnum.getTypeInfo());
        info.setScheduleTypes(ScheduleTypeEnum.getTypeInfo());
        info.setMisfireStrategies(MisfireStrategyEnum.getTypeInfo());
        info.setJobGroupList(jobGroupList);

        return JsonResponse.success(info);
    }

    @PostMapping("/pageList")
    @SaCheckPermission("job_task_read")
    public JsonResponse<LegoPage<XxlJobInfo>> pageList(@RequestBody JobConfigSearchVO vo) {
        return JsonResponse.success(xxlJobService.pageList(vo));
    }

    @PostMapping("/add")
    @SaCheckPermission("job_task_add")
    public JsonResponse<Object> add(@RequestBody XxlJobInfo jobInfo) {
        return JsonResponse.success(xxlJobService.add(jobInfo));
    }

    @PostMapping("/update")
    @SaCheckPermission("job_task_update")
    public JsonResponse<Object> update(@RequestBody XxlJobInfo jobInfo) {
        xxlJobService.update(jobInfo);
        return JsonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @SaCheckPermission("job_task_delete")
    public JsonResponse<Object> remove(@PathVariable int id) {
        xxlJobService.remove(id);
        return JsonResponse.success();
    }

    @PostMapping("/stop/{id}")
    @SaCheckPermission("job_task_update")
    public JsonResponse<Object> pause(@PathVariable int id) {
        xxlJobService.stop(id);
        return JsonResponse.success();
    }

    @PostMapping("/start/{id}")
    @SaCheckPermission("job_task_update")
    public JsonResponse<Object> start(@PathVariable int id) {
        xxlJobService.start(id);
        return JsonResponse.success();
    }

    @PostMapping("/trigger")
    @SaCheckPermission("job_task_update")
    public JsonResponse<Object> triggerJob(int id, String executorParam, String addressList) {
        xxlJobService.trigger(id, executorParam, addressList);
        return JsonResponse.success();
    }

    @SneakyThrows
    @GetMapping("/nextTriggerTime")
    @SaCheckPermission("job_task_read")
    public JsonResponse<List<String>> nextTriggerTime(String scheduleType, String scheduleConf) {
        XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
        paramXxlJobInfo.setScheduleType(scheduleType);
        paramXxlJobInfo.setScheduleConf(scheduleConf);

        List<String> result = new ArrayList<>();
        Date lastTime = new Date();
        for (int i = 0; i < 5; i++) {
            lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
            if (lastTime != null) {
                result.add(XxlDateUtil.formatDateTime(lastTime));
            } else {
                break;
            }
        }
        return JsonResponse.success(result);

    }

}
