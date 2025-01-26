package com.lego.job.service.impl;

import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.module.job.enums.ExecutorBlockStrategyEnum;
import com.lego.core.module.job.glue.GlueTypeEnum;
import com.lego.core.module.job.util.XxlDateUtil;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import com.lego.job.core.conf.XxlJobAdminConfig;
import com.lego.job.core.cron.CronExpression;
import com.lego.job.core.model.XxlJobGroup;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.core.model.XxlJobLogGlue;
import com.lego.job.core.model.XxlJobLogReport;
import com.lego.job.core.route.ExecutorRouteStrategyEnum;
import com.lego.job.core.scheduler.MisfireStrategyEnum;
import com.lego.job.core.scheduler.ScheduleTypeEnum;
import com.lego.job.core.thread.JobScheduleHelper;
import com.lego.job.core.thread.JobTriggerPoolHelper;
import com.lego.job.core.trigger.TriggerTypeEnum;
import com.lego.job.core.util.TemplateUtil;
import com.lego.job.mapper.XxlJobGroupMapper;
import com.lego.job.mapper.XxlJobInfoMapper;
import com.lego.job.mapper.XxlJobLogGlueMapper;
import com.lego.job.mapper.XxlJobLogMapper;
import com.lego.job.mapper.XxlJobLogReportMapper;
import com.lego.job.service.XxlJobService;
import com.lego.job.vo.JobConfigSearchVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl implements XxlJobService {

    private static Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;
    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    public XxlJobLogMapper xxlJobLogMapper;
    @Resource
    private XxlJobLogGlueMapper xxlJobLogGlueMapper;
    @Resource
    private XxlJobLogReportMapper xxlJobLogReportMapper;

    @Override
    public LegoPage<XxlJobInfo> pageList(JobConfigSearchVO vo) {

        // page list
        List<XxlJobInfo> list = xxlJobInfoMapper.pageList(vo.getOffset(), vo.getPageSize(), vo.getJobGroup(), vo.getTriggerStatus(), vo.getJobDesc(), vo.getExecutorHandler(), vo.getAuthor());
        int count = xxlJobInfoMapper.pageListCount(vo.getOffset(), vo.getPageSize(), vo.getJobGroup(), vo.getTriggerStatus(), vo.getJobDesc(), vo.getExecutorHandler(), vo.getAuthor());

        return new LegoPage<>(list, vo, count);
    }

    @Override
    public String add(XxlJobInfo jobInfo) {

        // valid base
        XxlJobGroup group = xxlJobGroupMapper.load(jobInfo.getJobGroup());
        if (group == null) {
            throw new BusinessException("请选择执行器");
        }
        if (jobInfo.getJobDesc() == null || jobInfo.getJobDesc().trim().length() == 0) {
            throw new BusinessException("请输入任务描述");
        }
        if (jobInfo.getAuthor() == null || jobInfo.getAuthor().trim().length() == 0) {
            throw new BusinessException("请输入负责人");
        }

        // valid trigger
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
        BusinessException.check(scheduleTypeEnum != null, "调度类型非法");
        if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
            if (jobInfo.getScheduleConf() == null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
                throw new BusinessException("Cron非法");
            }
        } else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE) {
            BusinessException.check(StringUtil.isNotBlank(jobInfo.getScheduleConf()), "调度类型为固定固定速度时必须填写时间信息");
            int fixSecond = Integer.valueOf(jobInfo.getScheduleConf());
            BusinessException.check(fixSecond >= 1, "调度时间格式非法");
        }

        // valid job
        GlueTypeEnum glueType = GlueTypeEnum.match(jobInfo.getGlueType());
        BusinessException.check(glueType != null, "运行模式非法");
        if (GlueTypeEnum.BEAN == glueType && (jobInfo.getExecutorHandler() == null || jobInfo.getExecutorHandler().trim().length() == 0)) {
            throw new BusinessException("请输入JobHandler");
        }

        String templateName = glueType.getTemplateName();
        if (StringUtil.isNotBlank(templateName)) {
            jobInfo.setGlueRemark("GLUE代码初始化");
            jobInfo.setGlueSource(TemplateUtil.getValue("vm/" + templateName));
        }
        // 》fix "\r" in shell
        if (GlueTypeEnum.GLUE_SHELL == glueType && jobInfo.getGlueSource() != null) {
            jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
        }

        // valid advanced
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            throw new BusinessException("路由策略非法");
        }
        if (MisfireStrategyEnum.match(jobInfo.getMisfireStrategy(), null) == null) {
            throw new BusinessException("调度过期策略非法");
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            throw new BusinessException("阻塞处理策略非法");
        }

        // 》ChildJobId valid
        if (jobInfo.getChildJobId() != null && jobInfo.getChildJobId().trim().length() > 0) {
            String[] childJobIds = jobInfo.getChildJobId().split(",");
            for (String childJobIdItem : childJobIds) {
                if (childJobIdItem != null && childJobIdItem.trim().length() > 0 && isNumeric(childJobIdItem)) {
                    XxlJobInfo childJobInfo = xxlJobInfoMapper.loadById(Integer.parseInt(childJobIdItem));
                    BusinessException.check(childJobInfo != null, "子任务ID({0})不存在", childJobIdItem);
                } else {
                    throw new BusinessException("子任务ID({0})非法", childJobIdItem);
                }
            }

            // join , avoid "xxx,,"
            String temp = "";
            for (String item : childJobIds) {
                temp += item + ",";
            }
            temp = temp.substring(0, temp.length() - 1);

            jobInfo.setChildJobId(temp);
        }

        // add in db
        jobInfo.setAddTime(new Date());
        jobInfo.setUpdateTime(new Date());
        jobInfo.setGlueUpdatetime(new Date());
        xxlJobInfoMapper.save(jobInfo);
        BusinessException.check(jobInfo.getId() >= 1, "新增失败");

        // log old code
        if (StringUtil.isNotBlank(jobInfo.getGlueSource())) {
            XxlJobLogGlue xxlJobLogGlue = new XxlJobLogGlue();
            xxlJobLogGlue.setJobId(jobInfo.getId());
            xxlJobLogGlue.setGlueType(jobInfo.getGlueType());
            xxlJobLogGlue.setGlueSource(jobInfo.getGlueSource());
            xxlJobLogGlue.setGlueRemark(jobInfo.getGlueRemark());
            xxlJobLogGlue.setAddTime(new Date());
            xxlJobLogGlue.setUpdateTime(new Date());
            xxlJobLogGlueMapper.save(xxlJobLogGlue);
        }

        return String.valueOf(jobInfo.getId());
    }

    private boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void update(XxlJobInfo jobInfo) {

        // valid base
        BusinessException.check(StringUtil.isNotBlank(jobInfo.getJobDesc()), "请输入任务描述");
        BusinessException.check(StringUtil.isNotBlank(jobInfo.getAuthor()), "请输入负责人");

        // valid trigger
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
        BusinessException.check(scheduleTypeEnum != null, "调度类型非法");
        if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
            if (jobInfo.getScheduleConf() == null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
                throw new BusinessException("Cron非法");
            }
        } else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE /*|| scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY*/) {
            BusinessException.check(StringUtil.isNotBlank(jobInfo.getScheduleConf()), "调度类型为固定时间时必须填写时间信息");
            int fixSecond = Integer.valueOf(jobInfo.getScheduleConf());
            BusinessException.check(fixSecond >= 1, "调度时间格式非法");
        }

        // valid advanced
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            throw new BusinessException("路由策略非法");
        }
        if (MisfireStrategyEnum.match(jobInfo.getMisfireStrategy(), null) == null) {
            throw new BusinessException("调度过期策略非法");
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            throw new BusinessException("阻塞处理策略非法");
        }

        // 》ChildJobId valid
        if (jobInfo.getChildJobId() != null && jobInfo.getChildJobId().trim().length() > 0) {
            String[] childJobIds = jobInfo.getChildJobId().split(",");
            for (String childJobIdItem : childJobIds) {
                if (childJobIdItem != null && childJobIdItem.trim().length() > 0 && isNumeric(childJobIdItem)) {
                    XxlJobInfo childJobInfo = xxlJobInfoMapper.loadById(Integer.parseInt(childJobIdItem));
                    BusinessException.check(childJobInfo != null, "子任务ID({0})不存在", childJobIdItem);
                } else {
                    throw new BusinessException("子任务ID({0})非法", childJobIdItem);
                }
            }

            // join , avoid "xxx,,"
            String temp = "";
            for (String item : childJobIds) {
                temp += item + ",";
            }
            temp = temp.substring(0, temp.length() - 1);

            jobInfo.setChildJobId(temp);
        }

        // group valid
        XxlJobGroup jobGroup = xxlJobGroupMapper.load(jobInfo.getJobGroup());
        BusinessException.check(jobGroup != null, "执行器非法，未获取到执行器[{0}]", jobInfo.getJobGroup());

        // stage job info
        XxlJobInfo exists_jobInfo = xxlJobInfoMapper.loadById(jobInfo.getId());
        BusinessException.check(exists_jobInfo != null, "任务[{0}]不存在", jobInfo.getId());

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = exists_jobInfo.getTriggerNextTime();
        boolean scheduleDataNotChanged = jobInfo.getScheduleType().equals(exists_jobInfo.getScheduleType()) && jobInfo.getScheduleConf().equals(exists_jobInfo.getScheduleConf());
        if (exists_jobInfo.getTriggerStatus() == 1 && !scheduleDataNotChanged) {
            try {
                Date nextValidTime = JobScheduleHelper.generateNextValidTime(jobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
                BusinessException.check(nextValidTime != null, "调度类型非法");
                nextTriggerTime = nextValidTime.getTime();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new BusinessException("调度类型非法非法");
            }
        }

        exists_jobInfo.setJobGroup(jobInfo.getJobGroup());
        exists_jobInfo.setJobDesc(jobInfo.getJobDesc());
        exists_jobInfo.setAuthor(jobInfo.getAuthor());
        exists_jobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
        exists_jobInfo.setScheduleType(jobInfo.getScheduleType());
        exists_jobInfo.setScheduleConf(jobInfo.getScheduleConf());
        exists_jobInfo.setMisfireStrategy(jobInfo.getMisfireStrategy());
        exists_jobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
        exists_jobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
        exists_jobInfo.setExecutorParam(jobInfo.getExecutorParam());
        exists_jobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        exists_jobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
        exists_jobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
        exists_jobInfo.setChildJobId(jobInfo.getChildJobId());
        exists_jobInfo.setTriggerNextTime(nextTriggerTime);

        exists_jobInfo.setUpdateTime(new Date());
        xxlJobInfoMapper.update(exists_jobInfo);
    }

    @Override
    public void remove(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
        if (xxlJobInfo == null) {
            return;
        }

        xxlJobInfoMapper.delete(id);
        xxlJobLogMapper.delete(id);
        xxlJobLogGlueMapper.deleteByJobId(id);
    }

    @Override
    public void start(int id) {
        BusinessException.check(XxlJobAdminConfig.isStarted(), "调度中心未启用，任务启动失败！");
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);

        // valid
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(xxlJobInfo.getScheduleType(), ScheduleTypeEnum.NONE);
        if (ScheduleTypeEnum.NONE == scheduleTypeEnum) {
            throw new BusinessException("当前调度类型禁止启动");
        }

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = 0;
        try {
            Date nextValidTime = JobScheduleHelper.generateNextValidTime(xxlJobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
            BusinessException.check(nextValidTime != null, "调度类型非法");
            nextTriggerTime = nextValidTime.getTime();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException("调度类型非法非法");
        }

        xxlJobInfo.setTriggerStatus(1);
        xxlJobInfo.setTriggerLastTime(0);
        xxlJobInfo.setTriggerNextTime(nextTriggerTime);

        xxlJobInfo.setUpdateTime(new Date());
        xxlJobInfoMapper.update(xxlJobInfo);
    }

    @Override
    public void stop(int id) {
        BusinessException.check(XxlJobAdminConfig.isStarted(), "调度中心未启用，任务停止失败！");
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);

        xxlJobInfo.setTriggerStatus(0);
        xxlJobInfo.setTriggerLastTime(0);
        xxlJobInfo.setTriggerNextTime(0);

        xxlJobInfo.setUpdateTime(new Date());
        xxlJobInfoMapper.update(xxlJobInfo);
    }

    @Override
    public void trigger(int jobId, String executorParam, String addressList) {
        BusinessException.check(XxlJobAdminConfig.isStarted(), "调度中心未启用，触发任务执行失败！");
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(jobId);
        BusinessException.check(xxlJobInfo != null, "任务ID({0})非法", jobId);
        // force cover job param
        if (executorParam == null) {
            executorParam = "";
        }

        JobTriggerPoolHelper.trigger(jobId, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);
    }

    @Override
    public Map<String, Object> dashboardInfo() {

        int jobInfoCount = xxlJobInfoMapper.findAllCount();
        int jobLogCount = 0;
        int jobLogSuccessCount = 0;
        XxlJobLogReport xxlJobLogReport = xxlJobLogReportMapper.queryLogReportTotal();
        if (xxlJobLogReport != null) {
            jobLogCount = xxlJobLogReport.getRunningCount() + xxlJobLogReport.getSucCount() + xxlJobLogReport.getFailCount();
            jobLogSuccessCount = xxlJobLogReport.getSucCount();
        }

        // executor count
        Set<String> executorAddressSet = new HashSet<String>();
        List<XxlJobGroup> groupList = xxlJobGroupMapper.findAll();

        if (groupList != null && !groupList.isEmpty()) {
            for (XxlJobGroup group : groupList) {
                if (group.getRegistryList() != null && !group.getRegistryList().isEmpty()) {
                    executorAddressSet.addAll(group.getRegistryList());
                }
            }
        }

        int executorCount = executorAddressSet.size();

        Map<String, Object> dashboardMap = new HashMap<String, Object>();
        dashboardMap.put("jobInfoCount", jobInfoCount);
        dashboardMap.put("jobLogCount", jobLogCount);
        dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
        dashboardMap.put("executorCount", executorCount);
        return dashboardMap;
    }

    @Override
    public Map<String, Object> chartInfo(Date startDate, Date endDate) {
        // process
        List<String> triggerDayList = new ArrayList<String>();
        List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
        List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
        List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
        int triggerCountRunningTotal = 0;
        int triggerCountSucTotal = 0;
        int triggerCountFailTotal = 0;
        if (endDate == null) {
            endDate = DateUtil.currentDate();
        }
        if (startDate == null) {
            startDate = XxlDateUtil.addDays(endDate, -7);
        }

        List<XxlJobLogReport> logReportList = xxlJobLogReportMapper.queryLogReport(startDate, endDate);

        if (logReportList != null && logReportList.size() > 0) {
            for (XxlJobLogReport item : logReportList) {
                String day = XxlDateUtil.formatDate(item.getTriggerDay());
                int triggerDayCountRunning = item.getRunningCount();
                int triggerDayCountSuc = item.getSucCount();
                int triggerDayCountFail = item.getFailCount();

                triggerDayList.add(day);
                triggerDayCountRunningList.add(triggerDayCountRunning);
                triggerDayCountSucList.add(triggerDayCountSuc);
                triggerDayCountFailList.add(triggerDayCountFail);

                triggerCountRunningTotal += triggerDayCountRunning;
                triggerCountSucTotal += triggerDayCountSuc;
                triggerCountFailTotal += triggerDayCountFail;
            }
        } else {
            for (int i = -6; i <= 0; i++) {
                triggerDayList.add(XxlDateUtil.formatDate(XxlDateUtil.addDays(new Date(), i)));
                triggerDayCountRunningList.add(0);
                triggerDayCountSucList.add(0);
                triggerDayCountFailList.add(0);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("triggerDayList", triggerDayList);
        result.put("triggerDayCountRunningList", triggerDayCountRunningList);
        result.put("triggerDayCountSucList", triggerDayCountSucList);
        result.put("triggerDayCountFailList", triggerDayCountFailList);

        result.put("triggerCountRunningTotal", triggerCountRunningTotal);
        result.put("triggerCountSucTotal", triggerCountSucTotal);
        result.put("triggerCountFailTotal", triggerCountFailTotal);

        return result;
    }

}
