package com.lego.job.controller;

import com.lego.core.exception.BusinessException;
import com.lego.core.job.biz.AdminBiz;
import com.lego.core.job.biz.model.HandleCallbackParam;
import com.lego.core.job.biz.model.RegistryParam;
import com.lego.core.job.biz.model.ReturnT;
import com.lego.core.job.util.GsonTool;
import com.lego.core.job.util.XxlJobRemotingUtil;
import com.lego.core.util.StringUtil;
import com.lego.job.core.conf.XxlJobAdminConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/back-end/job-api")
public class JobApiController {

    @Resource
    private AdminBiz adminBiz;

    /**
     * api
     *
     * @param uri
     * @param data
     * @return
     */
    @RequestMapping("/{uri}")
    @ResponseBody
    public ReturnT<String> api(HttpServletRequest request, @PathVariable("uri") String uri, @RequestBody(required = false) String data) {

        // valid
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, HttpMethod not support.");
        }
        if (uri == null || uri.trim().length() == 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping empty.");
        }
        String adminAccessToken = XxlJobAdminConfig.getAdminConfig().getAccessToken();
        BusinessException.check(StringUtil.isNotBlank(adminAccessToken), "job调度器未配置AccessToken");
        String clientAccessToken = request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN);
        if (!adminAccessToken.equals(clientAccessToken)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // services mapping
        if ("callback".equals(uri)) {
            List<HandleCallbackParam> callbackParamList = GsonTool.fromJson(data, List.class, HandleCallbackParam.class);
            return adminBiz.callback(callbackParamList);
        } else if ("registry".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
            return adminBiz.registry(registryParam);
        } else if ("registryRemove".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
            return adminBiz.registryRemove(registryParam);
        } else {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping(" + uri + ") not found.");
        }

    }

}
