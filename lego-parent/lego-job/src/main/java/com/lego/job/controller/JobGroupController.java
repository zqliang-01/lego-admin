package com.lego.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.module.job.enums.RegistryConfig;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.job.core.model.XxlJobGroup;
import com.lego.job.core.model.XxlJobRegistry;
import com.lego.job.mapper.XxlJobGroupMapper;
import com.lego.job.mapper.XxlJobInfoMapper;
import com.lego.job.mapper.XxlJobRegistryMapper;
import com.lego.job.vo.JobGroupSearchVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/back-end/job-group")
public class JobGroupController extends BaseController {

    @Resource
    public XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    public XxlJobGroupMapper xxlJobGroupMapper;
    @Resource
    private XxlJobRegistryMapper xxlJobRegistryMapper;

    @PostMapping("/list")
    @SaCheckPermission("job_executor_read")
    public JsonResponse<LegoPage<XxlJobGroup>> pageList(@RequestBody JobGroupSearchVO vo) {
        // page query
        List<XxlJobGroup> list = xxlJobGroupMapper.pageList(vo.getOffset(), vo.getPageSize(), vo.getAppname(), vo.getTitle());
        int count = xxlJobGroupMapper.pageListCount(vo.getOffset(), vo.getPageSize(), vo.getAppname(), vo.getTitle());
        return JsonResponse.success(new LegoPage<>(list, vo, count));
    }

    @PostMapping("/save")
    @SaCheckPermission("job_executor_add")
    public JsonResponse<Object> save(@RequestBody XxlJobGroup xxlJobGroup) {
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().length() == 0) {
            new BusinessException("请输入AppName");
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            new BusinessException("AppName长度限制为4~64");
        }
        if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
            new BusinessException("AppName非法");
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            new BusinessException("请输入名称");
        }
        if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
            new BusinessException("名称非法");
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
                new BusinessException("手动录入注册方式，机器地址不可为空");
            }
            if (xxlJobGroup.getAddressList().contains(">") || xxlJobGroup.getAddressList().contains("<")) {
                new BusinessException("机器地址非法");
            }

            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    new BusinessException("机器地址格式非法");
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupMapper.save(xxlJobGroup);
        return (ret > 0) ? JsonResponse.success() : JsonResponse.failed("保存失败！");
    }

    @PostMapping("/update")
    @SaCheckPermission("job_executor_update")
    public JsonResponse<Object> update(@RequestBody XxlJobGroup xxlJobGroup) {
        // valid
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().length() == 0) {
            new BusinessException("请输入AppName");
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            new BusinessException("AppName长度限制为4~64");
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            new BusinessException("请输入名称");
        }
        if (xxlJobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppname());
            String addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = "";
                for (String item : registryList) {
                    addressListStr += item + ",";
                }
                addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
            }
            xxlJobGroup.setAddressList(addressListStr);
        } else {
            // 1=手动录入
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
                new BusinessException("手动录入注册方式，机器地址不可为空");
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    new BusinessException("机器地址格式非法");
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupMapper.update(xxlJobGroup);
        return (ret > 0) ? JsonResponse.success() : JsonResponse.failed("更新失败！");
    }

    private List<String> findRegistryByAppName(String appnameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
        List<XxlJobRegistry> list = xxlJobRegistryMapper.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
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
        return appAddressMap.get(appnameParam);
    }

    @PostMapping("/remove/{id}")
    @SaCheckPermission("job_executor_delete")
    public JsonResponse<Object> remove(@PathVariable int id) {
        // valid
        int count = xxlJobInfoMapper.pageListCount(0, 10, id, -1, null, null, null);
        BusinessException.check(count <= 0, "拒绝删除，该执行器使用中");

        List<XxlJobGroup> allList = xxlJobGroupMapper.findAll();
        BusinessException.check(allList.size() != 1, "拒绝删除, 系统至少保留一个执行器");

        int ret = xxlJobGroupMapper.remove(id);
        return (ret > 0) ? JsonResponse.success() : JsonResponse.failed("删除失败！");
    }

    @GetMapping("/loadById/{id}")
    @SaCheckPermission("job_task_read")
    public JsonResponse<XxlJobGroup> loadById(@PathVariable int id) {
        XxlJobGroup jobGroup = xxlJobGroupMapper.load(id);
        BusinessException.check(jobGroup != null, "根据[]未查询到分组结果!", id);
        return JsonResponse.success(jobGroup);
    }

}
