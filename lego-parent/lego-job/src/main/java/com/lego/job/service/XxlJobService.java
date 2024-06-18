package com.lego.job.service;


import com.lego.core.dto.LegoPage;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.vo.JobConfigSearchVO;

import java.util.Date;
import java.util.Map;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {

    LegoPage<XxlJobInfo> pageList(JobConfigSearchVO vo);

    /**
     * add job
     *
     * @param jobInfo
     * @return
     */
    String add(XxlJobInfo jobInfo);

    /**
     * update job
     *
     * @param jobInfo
     * @return
     */
    void update(XxlJobInfo jobInfo);

    /**
     * remove job
     * *
     *
     * @param id
     * @return
     */
    void remove(int id);

    /**
     * start job
     *
     * @param id
     * @return
     */
    void start(int id);

    /**
     * stop job
     *
     * @param id
     * @return
     */
    void stop(int id);

    /**
     * trigger
     *
     * @param loginUser
     * @param jobId
     * @param executorParam
     * @param addressList
     * @return
     */
    void trigger(int jobId, String executorParam, String addressList);

    /**
     * dashboard info
     *
     * @return
     */
    Map<String, Object> dashboardInfo();

    /**
     * chart info
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, Object> chartInfo(Date startDate, Date endDate);

}
