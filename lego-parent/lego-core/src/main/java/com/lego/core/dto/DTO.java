package com.lego.core.dto;

import java.io.Serializable;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class DTO implements Serializable {
    private static final long serialVersionUID = 8478873767622245823L;

	public String toJson() {
        String json = JSON.toJSONString(this);
        log.debug("生成JSON数据：" + json);
        return json;
    }

    /**
     * dto转为xml字符串
     */
    public String toXml() throws Exception {
        JAXBContext context = JAXBContext.newInstance(this.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); // 编码格式
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 是否格式化生成的xml串
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true); // 是否省略xml头信息
        StringWriter writer = new StringWriter();
        marshaller.marshal(this.getClass(), writer);
        return writer.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }
}
