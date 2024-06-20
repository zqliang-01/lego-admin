package com.lego.job.core.route.strategy;

import com.lego.core.job.biz.ExecutorBiz;
import com.lego.core.job.biz.model.ReturnT;
import com.lego.core.job.biz.model.TriggerParam;
import com.lego.job.core.route.ExecutorRouter;
import com.lego.job.core.scheduler.XxlJobScheduler;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFailover extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {

        StringBuffer beatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            ReturnT<String> beatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
                beatResult = executorBiz.beat();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                beatResult = new ReturnT<String>(ReturnT.FAIL_CODE, "" + e);
            }
            beatResultSB.append((beatResultSB.length() > 0) ? "<br><br>" : "")
                .append("心跳检测：")
                .append("<br>address：").append(address)
                .append("<br>code：").append(beatResult.getCode())
                .append("<br>msg：").append(beatResult.getMsg());

            // beat success
            if (beatResult.getCode() == ReturnT.SUCCESS_CODE) {

                beatResult.setMsg(beatResultSB.toString());
                beatResult.setContent(address);
                return beatResult;
            }
        }
        return new ReturnT<String>(ReturnT.FAIL_CODE, beatResultSB.toString());

    }
}
