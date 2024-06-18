package com.lego.job.core.scheduler;

import com.lego.core.dto.TypeInfo;
import com.lego.job.core.util.I18nUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxueli 2020-10-29 21:11:23
 */
public enum ScheduleTypeEnum {

    NONE(I18nUtil.getString("schedule_type_none")),

    /**
     * schedule by cron
     */
    CRON(I18nUtil.getString("schedule_type_cron")),

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE(I18nUtil.getString("schedule_type_fix_rate")),

    /**
     * schedule by fix delay (in seconds)， after the last time
     */
    /*FIX_DELAY(I18nUtil.getString("schedule_type_fix_delay"))*/;

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
