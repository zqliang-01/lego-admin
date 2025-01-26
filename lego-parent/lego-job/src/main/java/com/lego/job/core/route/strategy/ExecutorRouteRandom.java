package com.lego.job.core.route.strategy;

import com.lego.core.module.job.biz.model.ReturnT;
import com.lego.core.module.job.biz.model.TriggerParam;
import com.lego.job.core.route.ExecutorRouter;

import java.util.List;
import java.util.Random;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
