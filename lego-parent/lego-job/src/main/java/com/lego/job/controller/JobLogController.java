package com.lego.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.job.biz.ExecutorBiz;
import com.lego.core.job.biz.model.KillParam;
import com.lego.core.job.biz.model.LogParam;
import com.lego.core.job.biz.model.LogResult;
import com.lego.core.job.biz.model.ReturnT;
import com.lego.core.job.util.XxlDateUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.job.core.complete.XxlJobCompleter;
import com.lego.job.core.model.XxlJobGroup;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.core.model.XxlJobLog;
import com.lego.job.core.scheduler.XxlJobScheduler;
import com.lego.job.dto.JobLogInfo;
import com.lego.job.mapper.XxlJobGroupMapper;
import com.lego.job.mapper.XxlJobInfoMapper;
import com.lego.job.mapper.XxlJobLogMapper;
import com.lego.job.vo.JobLogSearchVO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/back-end/job-log")
public class JobLogController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(JobLogController.class);

    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;
    @Resource
    public XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    public XxlJobLogMapper xxlJobLogMapper;

    @GetMapping("/index")
    @SaCheckPermission("job_log_read")
    public JsonResponse<JobLogInfo> index(Model model, @RequestParam(required = false, defaultValue = "0") Integer jobId) {

        // 执行器列表
        List<XxlJobGroup> jobGroupList = xxlJobGroupMapper.findAll();

        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new BusinessException("不存在有效执行器，请联系管理员");
        }

        JobLogInfo info = new JobLogInfo();
        info.setJobGroupList(jobGroupList);

        // 任务
        if (jobId > 0) {
            XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(jobId);
            BusinessException.check(jobInfo != null, "任务[{0}]不存在", jobId);
            info.setJobInfo(jobInfo);
        }

        return JsonResponse.success(info);
    }

    @GetMapping("/getJobsByGroup")
    @SaCheckPermission("job_log_read")
    public JsonResponse<List<XxlJobInfo>> getJobsByGroup(int jobGroup) {
        return JsonResponse.success(xxlJobInfoMapper.getJobsByGroup(jobGroup));
    }

    @PostMapping("/pageList")
    @SaCheckPermission("job_log_read")
    public JsonResponse<LegoPage<XxlJobLog>> pageList(@RequestBody JobLogSearchVO vo) {
        Date triggerTimeStart = null;
        Date triggerTimeEnd = null;
        if (StringUtil.isNotBlank(vo.getFilterTime())) {
            String[] temp = vo.getFilterTime().split(" - ");
            if (temp.length == 2) {
                triggerTimeStart = XxlDateUtil.parseDateTime(temp[0]);
                triggerTimeEnd = XxlDateUtil.parseDateTime(temp[1]);
            }
        }

        List<XxlJobLog> list = xxlJobLogMapper.pageList(vo.getOffset(), vo.getPageSize(), vo.getJobGroup(), vo.getJobId(), triggerTimeStart, triggerTimeEnd, vo.getLogStatus());
        int count = xxlJobLogMapper.pageListCount(vo.getOffset(), vo.getPageSize(), vo.getJobGroup(), vo.getJobId(), triggerTimeStart, triggerTimeEnd, vo.getLogStatus());
        return JsonResponse.success(new LegoPage<>(list, vo, count));
    }

    @SneakyThrows
    @GetMapping("/logDetailCat")
    @SaCheckPermission("job_log_read")
    public JsonResponse<LogResult> logDetailCat(long logId, int fromLineNum) {
        XxlJobLog jobLog = xxlJobLogMapper.load(logId);    // todo, need to improve performance
        BusinessException.check(jobLog != null, "日志ID非法");

        // log cat
        ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(jobLog.getExecutorAddress());
        BusinessException.check(executorBiz != null, "执行器地址[{0}]无可用执行器，日志查询失败！", jobLog.getExecutorAddress());

        ReturnT<LogResult> logResult = executorBiz.log(new LogParam(jobLog.getTriggerTime().getTime(), logId, fromLineNum));
        BusinessException.check(logResult.getCode() == ReturnT.SUCCESS_CODE, logResult.getMsg());

        // is end
        LogResult content = logResult.getContent();
        if (content != null && content.getFromLineNum() > content.getToLineNum()) {
            if (jobLog.getHandleCode() > 0) {
                content.setEnd(true);
            }
        }

        // fix xss
        if (content != null && StringUtils.hasText(content.getLogContent())) {
            String newLogContent = content.getLogContent();
            newLogContent = HtmlUtils.htmlEscape(newLogContent, "UTF-8");
            content.setLogContent(newLogContent);
        }

        return JsonResponse.success(content);
    }

    @SneakyThrows
    @PostMapping("/logKill/{id}")
    @SaCheckPermission("job_log_delete")
    public JsonResponse<String> logKill(@PathVariable int id) {
        XxlJobLog log = xxlJobLogMapper.load(id);
        XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(log.getJobId());
        BusinessException.check(jobInfo != null, "任务ID非法");
        if (ReturnT.SUCCESS_CODE != log.getTriggerCode()) {
            new BusinessException("调度失败，无法终止日志");
        }

        ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(log.getExecutorAddress());
        ReturnT<String> runResult = executorBiz.kill(new KillParam(jobInfo.getId()));
        BusinessException.check(ReturnT.SUCCESS_CODE == runResult.getCode(), runResult.getMsg());

        log.setHandleCode(ReturnT.FAIL_CODE);
        log.setHandleMsg("人为操作，主动终止:" + (runResult.getMsg() != null ? runResult.getMsg() : ""));
        log.setHandleTime(new Date());
        XxlJobCompleter.updateHandleInfoAndFinish(log);
        return JsonResponse.success(runResult.getMsg());
    }

    @PostMapping("/clearLog")
    @SaCheckPermission("job_log_delete")
    public JsonResponse<Object> clearLog(int jobGroup, int jobId, int type) {

        Date clearBeforeTime = null;
        int clearBeforeNum = 0;
        if (type == 1) {
            clearBeforeTime = XxlDateUtil.addMonths(new Date(), -1);    // 清理一个月之前日志数据
        } else if (type == 2) {
            clearBeforeTime = XxlDateUtil.addMonths(new Date(), -3);    // 清理三个月之前日志数据
        } else if (type == 3) {
            clearBeforeTime = XxlDateUtil.addMonths(new Date(), -6);    // 清理六个月之前日志数据
        } else if (type == 4) {
            clearBeforeTime = XxlDateUtil.addYears(new Date(), -1);    // 清理一年之前日志数据
        } else if (type == 5) {
            clearBeforeNum = 1000;        // 清理一千条以前日志数据
        } else if (type == 6) {
            clearBeforeNum = 10000;        // 清理一万条以前日志数据
        } else if (type == 7) {
            clearBeforeNum = 30000;        // 清理三万条以前日志数据
        } else if (type == 8) {
            clearBeforeNum = 100000;    // 清理十万条以前日志数据
        } else if (type == 9) {
            clearBeforeNum = 0;            // 清理所有日志数据
        } else {
            new BusinessException("清理类型参数异常");
        }

        List<Long> logIds = null;
        do {
            logIds = xxlJobLogMapper.findClearLogIds(jobGroup, jobId, clearBeforeTime, clearBeforeNum, 1000);
            if (logIds != null && logIds.size() > 0) {
                xxlJobLogMapper.clearLog(logIds);
            }
        } while (logIds != null && logIds.size() > 0);

        return JsonResponse.success();
    }

}
