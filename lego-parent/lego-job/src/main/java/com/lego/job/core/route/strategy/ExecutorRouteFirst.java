package com.lego.job.core.route.strategy;

import com.lego.core.module.job.biz.model.ReturnT;
import com.lego.core.module.job.biz.model.TriggerParam;
import com.lego.job.core.route.ExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(0));
    }

}
