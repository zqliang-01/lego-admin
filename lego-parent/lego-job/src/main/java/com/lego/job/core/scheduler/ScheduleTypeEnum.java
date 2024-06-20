package com.lego.job.core.scheduler;

import com.lego.core.dto.TypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxueli 2020-10-29 21:11:23
 */
public enum ScheduleTypeEnum {

    NONE("无"),

    /**
     * schedule by cron
     */
    CRON("CRON"),

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE("固定速度"),

    /**
     * schedule by fix delay (in seconds)， after the last time
     */
    /*FIX_DELAY("固定延迟")*/;

    private String title;

    ScheduleTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static ScheduleTypeEnum match(String name, ScheduleTypeEnum defaultItem) {
        for (ScheduleTypeEnum item : ScheduleTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

    public static List<TypeInfo> getTypeInfo() {
        List<TypeInfo> infos = new ArrayList<>();
        for (ScheduleTypeEnum item : values()) {
            infos.add(new TypeInfo(item.name(), item.title));
        }
        return infos;
    }
}
