package com.lego.job.core.scheduler;

import com.lego.core.dto.TypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxueli 2020-10-29 21:11:23
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING("忽略"),

    /**
     * fire once now
     */
    FIRE_ONCE_NOW("立即执行一次");

    private String title;

    MisfireStrategyEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static MisfireStrategyEnum match(String name, MisfireStrategyEnum defaultItem) {
        for (MisfireStrategyEnum item : MisfireStrategyEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

    public static List<TypeInfo> getTypeInfo() {
        List<TypeInfo> infos = new ArrayList<>();
        for (MisfireStrategyEnum item : values()) {
            infos.add(new TypeInfo(item.name(), item.title));
        }
        return infos;
    }
}
