package com.lego.job.core.model;

import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xuxueli on 16/9/30.
 */
public class XxlJobGroup {

    private int id;
    private String appname;
    private String title;
    private int addressType;        // 执行器地址类型：0=自动注册、1=手动录入
    private String addressList;     // 执行器地址列表，多地址逗号分隔(手动录入)
    private Date updateTime;

    // registry list
    private List<String> registryList;  // 执行器地址列表(系统注册)

    public List<String> getRegistryList() {
        if (addressList != null && addressList.trim().length() > 0) {
            registryList = new ArrayList<>();
            for (String address : Arrays.asList(addressList.split(","))) {
                if (StringUtil.isNotBlank(address) && address.startsWith("lb:")) {
                    String serviceName = address.replace("lb://", "");
                    DiscoveryClient discoveryClient = LegoBeanFactory.getBean(DiscoveryClient.class);
                    List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                    List<String> uriList = instances.stream().map(a -> a.getUri().toString()).collect(Collectors.toList());
                    registryList.addAll(uriList);
                    continue;
                }
                registryList.add(address);
            }
        }
        return registryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

}
