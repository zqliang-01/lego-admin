package com.lego.core.module.job.biz;

import com.lego.core.module.job.biz.model.IdleBeatParam;
import com.lego.core.module.job.biz.model.KillParam;
import com.lego.core.module.job.biz.model.LogParam;
import com.lego.core.module.job.biz.model.LogResult;
import com.lego.core.module.job.biz.model.ReturnT;
import com.lego.core.module.job.biz.model.TriggerParam;

/**
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat
     *
     * @return
     */
    public ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param idleBeatParam
     * @return
     */
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     *
     * @param triggerParam
     * @return
     */
    public ReturnT<String> run(TriggerParam triggerParam);

    /**
     * kill
     *
     * @param killParam
     * @return
     */
    public ReturnT<String> kill(KillParam killParam);

    /**
     * log
     *
     * @param logParam
     * @return
     */
    public ReturnT<LogResult> log(LogParam logParam);

}
