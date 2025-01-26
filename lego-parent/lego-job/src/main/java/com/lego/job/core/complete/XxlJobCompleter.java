package com.lego.job.core.complete;

import com.lego.core.module.job.biz.model.ReturnT;
import com.lego.core.module.job.context.XxlJobContext;
import com.lego.job.core.conf.XxlJobAdminConfig;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.core.model.XxlJobLog;
import com.lego.job.core.thread.JobTriggerPoolHelper;
import com.lego.job.core.trigger.TriggerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author xuxueli 2020-10-30 20:43:10
 */
public class XxlJobCompleter {

    private static Logger logger = LoggerFactory.getLogger(XxlJobCompleter.class);

    /**
     * common fresh handle entrance (limit only once)
     *
     * @param xxlJobLog
     * @return
     */
    public static int updateHandleInfoAndFinish(XxlJobLog xxlJobLog) {

        // finish
        finishJob(xxlJobLog);

        // text最大64kb 避免长度过长
        if (xxlJobLog.getHandleMsg().length() > 15000) {
            xxlJobLog.setHandleMsg(xxlJobLog.getHandleMsg().substring(0, 15000));
        }

        // fresh handle
        return XxlJobAdminConfig.getAdminConfig().getXxlJobLogMapper().updateHandleInfo(xxlJobLog);
    }


    /**
     * do somethind to finish job
     */
    private static void finishJob(XxlJobLog xxlJobLog) {

        // 1、handle success, to trigger child job
        String triggerChildMsg = null;
        if (XxlJobContext.HANDLE_CODE_SUCCESS == xxlJobLog.getHandleCode()) {
            XxlJobInfo xxlJobInfo = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoMapper().loadById(xxlJobLog.getJobId());
            if (xxlJobInfo != null && xxlJobInfo.getChildJobId() != null && xxlJobInfo.getChildJobId().trim().length() > 0) {
                triggerChildMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发子任务<<<<<<<<<<< </span><br>";

                String[] childJobIds = xxlJobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    int childJobId = (childJobIds[i] != null && childJobIds[i].trim().length() > 0 && isNumeric(childJobIds[i])) ? Integer.valueOf(childJobIds[i]) : -1;
                    if (childJobId > 0) {

                        JobTriggerPoolHelper.trigger(childJobId, TriggerTypeEnum.PARENT, -1, null, null, null);
                        ReturnT<String> triggerChildResult = ReturnT.SUCCESS;

                        // add msg
                        triggerChildMsg += MessageFormat.format("{0}/{1} [任务ID={2}], 触发{3}, 触发备注: {4} <br>",
                            (i + 1),
                            childJobIds.length,
                            childJobIds[i],
                            (triggerChildResult.getCode() == ReturnT.SUCCESS_CODE ? "成功" : "失败"),
                            triggerChildResult.getMsg());
                    } else {
                        triggerChildMsg += MessageFormat.format("{0}/{1} [任务ID={2}], 触发失败, 触发备注: 任务ID格式错误 <br>",
                            (i + 1),
                            childJobIds.length,
                            childJobIds[i]);
                    }
                }

            }
        }

        if (triggerChildMsg != null) {
            xxlJobLog.setHandleMsg(xxlJobLog.getHandleMsg() + triggerChildMsg);
        }

        // 2、fix_delay trigger next
        // on the way

    }

    private static boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
