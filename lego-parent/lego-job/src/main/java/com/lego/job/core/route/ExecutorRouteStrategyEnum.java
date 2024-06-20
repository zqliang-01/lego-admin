package com.lego.job.core.route;

import com.lego.core.dto.TypeInfo;
import com.lego.job.core.route.strategy.ExecutorRouteBusyover;
import com.lego.job.core.route.strategy.ExecutorRouteConsistentHash;
import com.lego.job.core.route.strategy.ExecutorRouteFailover;
import com.lego.job.core.route.strategy.ExecutorRouteFirst;
import com.lego.job.core.route.strategy.ExecutorRouteLFU;
import com.lego.job.core.route.strategy.ExecutorRouteLRU;
import com.lego.job.core.route.strategy.ExecutorRouteLast;
import com.lego.job.core.route.strategy.ExecutorRouteRandom;
import com.lego.job.core.route.strategy.ExecutorRouteRound;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public enum ExecutorRouteStrategyEnum {

    FIRST("第一个", new ExecutorRouteFirst()),
    LAST("最后一个", new ExecutorRouteLast()),
    ROUND("轮询", new ExecutorRouteRound()),
    RANDOM("随机", new ExecutorRouteRandom()),
    CONSISTENT_HASH("一致性HASH", new ExecutorRouteConsistentHash()),
    LEAST_FREQUENTLY_USED("最不经常使用", new ExecutorRouteLFU()),
    LEAST_RECENTLY_USED("最近最久未使用", new ExecutorRouteLRU()),
    FAILOVER("故障转移", new ExecutorRouteFailover()),
    BUSYOVER("忙碌转移", new ExecutorRouteBusyover()),
    SHARDING_BROADCAST("分片广播", null);

    ExecutorRouteStrategyEnum(String title, ExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private String title;
    private ExecutorRouter router;

    public String getTitle() {
        return title;
    }

    public ExecutorRouter getRouter() {
        return router;
    }

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem) {
        if (name != null) {
            for (ExecutorRouteStrategyEnum item : ExecutorRouteStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

    public static List<TypeInfo> getTypeInfo() {
        List<TypeInfo> infos = new ArrayList<>();
        for (ExecutorRouteStrategyEnum item : values()) {
            infos.add(new TypeInfo(item.name(), item.title));
        }
        return infos;
    }
}
