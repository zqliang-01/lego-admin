package com.lego.job.core.thread;

import com.lego.core.module.job.biz.model.RegistryParam;
import com.lego.core.module.job.biz.model.ReturnT;
import com.lego.core.module.job.enums.RegistryConfig;
import com.lego.job.core.conf.XxlJobAdminConfig;
import com.lego.job.core.model.XxlJobGroup;
import com.lego.job.core.model.XxlJobRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * job registry instance
 *
 * @author xuxueli 2016-10-02 19:10:24
 */
public class JobRegistryHelper {

    private static Logger logger = LoggerFactory.getLogger(JobRegistryHelper.class);

    private static JobRegistryHelper instance = new JobRegistryHelper();

    public static JobRegistryHelper getInstance() {
        return instance;
    }

    private ThreadPoolExecutor registryOrRemoveThreadPool = null;
    private Thread registryMonitorThread;
    private volatile boolean toStop = false;

    public void start() {

        // for registry or remove
        registryOrRemoveThreadPool = new ThreadPoolExecutor(
            2,
            10,
            30L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(2000),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "xxl-job, admin JobRegistryMonitorHelper-registryOrRemoveThreadPool-" + r.hashCode());
                }
            },
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    r.run();
                    logger.warn(">>>>>>>>>>> xxl-job, registry or remove too fast, match threadpool rejected handler(run now).");
                }
            });

        // for monitor
        registryMonitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!toStop) {
                    try {
                        // auto registry group
                        List<XxlJobGroup> groupList = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupMapper().findByAddressType(0);
                        if (groupList != null && !groupList.isEmpty()) {

                            // remove dead address (admin/executor)
                            List<Integer> ids = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().findDead(RegistryConfig.DEAD_TIMEOUT, new Date());
                            if (ids != null && ids.size() > 0) {
                                XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().removeDead(ids);
                            }

                            // fresh online address (admin/executor)
                            HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
                            List<XxlJobRegistry> list = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
                            if (list != null) {
                                for (XxlJobRegistry item : list) {
                                    if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                                        String appname = item.getRegistryKey();
                                        List<String> registryList = appAddressMap.get(appname);
                                        if (registryList == null) {
                                            registryList = new ArrayList<String>();
                                        }

                                        if (!registryList.contains(item.getRegistryValue())) {
                                            registryList.add(item.getRegistryValue());
                                        }
                                        appAddressMap.put(appname, registryList);
                                    }
                                }
                            }

                            // fresh group address
                            for (XxlJobGroup group : groupList) {
                                List<String> registryList = appAddressMap.get(group.getAppname());
                                String addressListStr = null;
                                if (registryList != null && !registryList.isEmpty()) {
                                    Collections.sort(registryList);
                                    StringBuilder addressListSB = new StringBuilder();
                                    for (String item : registryList) {
                                        addressListSB.append(item).append(",");
                                    }
                                    addressListStr = addressListSB.toString();
                                    addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
                                }
                                group.setAddressList(addressListStr);
                                group.setUpdateTime(new Date());

                                XxlJobAdminConfig.getAdminConfig().getXxlJobGroupMapper().update(group);
                            }
                        }
                    } catch (Exception e) {
                        if (!toStop) {
                            logger.error(">>>>>>>>>>> xxl-job, job registry monitor thread error:{}", e);
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    } catch (InterruptedException e) {
                        if (!toStop) {
                            logger.error(">>>>>>>>>>> xxl-job, job registry monitor thread error:{}", e);
                        }
                    }
                }
                logger.info(">>>>>>>>>>> xxl-job, job registry monitor thread stop");
            }
        });
        registryMonitorThread.setDaemon(true);
        registryMonitorThread.setName("xxl-job, admin JobRegistryMonitorHelper-registryMonitorThread");
        registryMonitorThread.start();
    }

    public void toStop() {
        toStop = true;

        // stop registryOrRemoveThreadPool
        registryOrRemoveThreadPool.shutdownNow();

        // stop monitir (interrupt and wait)
        registryMonitorThread.interrupt();
        try {
            registryMonitorThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    // ---------------------- helper ----------------------

    public ReturnT<String> registry(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
            || !StringUtils.hasText(registryParam.getRegistryKey())
            || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }

        // async execute
        registryOrRemoveThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                int ret = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().registryUpdate(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue(), new Date());
                if (ret < 1) {
                    XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().registrySave(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue(), new Date());

                    // fresh
                    freshGroupRegistryInfo(registryParam);
                }
            }
        });

        return ReturnT.SUCCESS;
    }

    public ReturnT<String> registryRemove(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
            || !StringUtils.hasText(registryParam.getRegistryKey())
            || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }

        // async execute
        registryOrRemoveThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                int ret = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().registryDelete(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
                if (ret > 0) {
                    // fresh
                    freshGroupRegistryInfo(registryParam);
                }
            }
        });

        return ReturnT.SUCCESS;
    }

    private void freshGroupRegistryInfo(RegistryParam registryParam) {
        // Under consideration, prevent affecting core tables
    }


}
