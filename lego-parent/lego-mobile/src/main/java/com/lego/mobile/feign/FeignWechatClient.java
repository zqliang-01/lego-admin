package com.lego.mobile.feign;

import com.lego.mobile.dto.MobileWechatOpenidInfo;
import com.lego.mobile.vo.WechatOpenidVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "wechatClient", url = "https://api.weixin.qq.com")
public interface FeignWechatClient {

    @GetMapping(value = "/sns/oauth2/access_token", produces = MediaType.APPLICATION_JSON_VALUE)
    MobileWechatOpenidInfo code2session(@SpringQueryMap WechatOpenidVO vo);

}
