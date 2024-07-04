package com.lego.sharding.config;

import com.lego.core.util.StringUtil;
import org.apache.shardingsphere.infra.hint.HintManager;

public class ShardingHintConfig {

    private static final ThreadLocal<HintManager> HINT_MANAGER_HOLDER = new ThreadLocal<>();

    public static HintManager getInstance() {
        HintManager hintManager = HINT_MANAGER_HOLDER.get();
        if (hintManager == null) {
            hintManager = HintManager.getInstance();
            HINT_MANAGER_HOLDER.set(hintManager);
            return hintManager;
        }
        return hintManager;
    }

    /**
     * 强制使用指定数据源，注意：会覆盖路由策略中配置的数据源
     */
    public static void setDataSource(String name) {
        if (StringUtil.isNotBlank(name)) {
            getInstance().setDataSourceName(name);
        }
    }

    public static void close() {
        HintManager hintManager = HINT_MANAGER_HOLDER.get();
        if (hintManager != null) {
            hintManager.close();
        }
    }

    public static void clear() {
        HintManager hintManager = HINT_MANAGER_HOLDER.get();
        if (hintManager != null) {
            hintManager.close();
            HINT_MANAGER_HOLDER.remove();
        }
    }
}
