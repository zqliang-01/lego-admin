package com.lego.mobile.feign;

import com.lego.mobile.dto.MobileWechatMpOpenidInfo;
import com.lego.mobile.vo.WechatMpOpenidVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "wechatMpClient", url = "https://api.weixin.qq.com")
public interface FeignWechatMpClient {

    @GetMapping(value = "/sns/jscode2session", produces = MediaType.APPLICATION_JSON_VALUE)
    MobileWechatMpOpenidInfo code2session(@SpringQueryMap WechatMpOpenidVO vo);

}
