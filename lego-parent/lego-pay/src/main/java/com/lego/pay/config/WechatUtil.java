package com.lego.pay.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.lego.core.common.Constants;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import com.lego.pay.message.WechatMicroPayRequestMessage;
import com.lego.pay.message.WechatOrderQueryRequestMessage;
import com.lego.pay.message.WechatOrderRefundRequestMessage;
import com.lego.pay.message.WechatPrePayOrderRequestMessage;
import com.lego.pay.message.WechatReconciliationBillRequestMessage;
import com.lego.pay.vo.WechatPreOrderVO;


/**
 * 微信工具类，用于微信签名
 */
public class WechatUtil {
    protected final static Logger log = LoggerFactory.getLogger(WechatUtil.class);
    private final static String DEFAULT_CHARSET = "iso-8859-1";
    public static final String WEIXIN_SUCCESS = "success";
    public static final String WEIXIN_RETURN_SUCCESS = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml>";
    public static String WEIXIN_RETURN_FAIL = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + " <return_msg><![CDATA[{0}]]></return_msg>" + "</xml>";

    public static String sslExecute(String message, String url) {
        String mchId = WechatPayConfig.getMchId();
        String certPath = WechatPayConfig.getCertPath();

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(new File(certPath)), mchId.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();

            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(message, DEFAULT_CHARSET));

            response = httpclient.execute(post);
            StringBuilder responsestr = new StringBuilder();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), Constants.DEFAULT_CHARSET_NAME));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    responsestr.append(text);
                }
            }
            EntityUtils.consume(entity);
            return responsestr.toString();
        }
        catch (Exception e) {
            throw new CoreException("wechat-refund-certificate-error", e);
        }
    }

	public static <T> T convertToBean(String xml, Class<T> clazz) {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
		try {
			return xmlMapper.readValue(xml, clazz);
		}
		catch (IOException e) {
			throw new CoreException("wechat-refund-certificate-error", e);
		}
	}

	public static String convertToXML(Object bean) {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
		try {
			return xmlMapper.writeValueAsString(bean);
		}
		catch (JsonProcessingException e) {
			throw new CoreException("wechat-refund-certificate-error", e);
		}
	}

	public static Map<String, String> convertToSortMap(Object bean) {
		try {
            Class<?> clazz = bean.getClass();
            Set<Field> fieldList = new HashSet<Field>();
            while (clazz != Object.class){
              fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
              clazz = clazz.getSuperclass();
            }
            TreeMap<String, String> sortMap = getMapKeyComparator();
            for (Field field : fieldList) {
            	JacksonXmlProperty element = field.getAnnotation(JacksonXmlProperty.class);
                field.setAccessible(true);
                Object object = field.get(bean);
                if (element != null && object != null) {
                	sortMap.put(element.localName(), StringUtil.toString(object));
                }
            }
            return sortMap;
        }
        catch (Exception e) {
            throw new CoreException("wechat-sign-error", e);
        }
    }

    public static String paySign(Object bean, String mchKey) {
    	Map<String, String> sortMap = convertToSortMap(bean);
        String sign = "";
        for (String key : sortMap.keySet()) {
            if (StringUtil.isNotBlank(sortMap.get(key)) && !"sign".equals(key)) {
                sign = sign + key + "=" + sortMap.get(key) + "&";
            }
        }
        sign = sign + "key=" + mchKey;
        return StringUtil.getMD5(sign).toUpperCase();
    }

    public static TreeMap<String, String> getMapKeyComparator() {
        return new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
		        return o1.compareTo(o2);
			}
		});
    }

    /**
     * 微信预支付下单
     */
    public static WechatPrePayOrderRequestMessage creatWeChatPrePayOrderRequestMessage(WechatPreOrderVO requestVo) {
        WechatPrePayOrderRequestMessage requestMessage = new WechatPrePayOrderRequestMessage();
        requestMessage.setNonceStr(StringUtil.getRandomString(8));
        requestMessage.setSpbillCreateIp(requestVo.getRequestIp());
        requestMessage.setAppid(WechatPayConfig.getAppId());
        requestMessage.setAttach(requestVo.getBody());
        if (StringUtil.isBlank(requestVo.getBody())) {
        	requestVo.setBody("Lego");
        }
        requestMessage.setBody(requestVo.getBody());
        requestMessage.setMchId(WechatPayConfig.getMchId());
        requestMessage.setNotifyUrl(WechatPayConfig.getNotifyUrl());
        if (WechatPayConfig.JS_TRADE_TYPE.equals(requestVo.getTradeType())) {
            requestMessage.setOpenid(requestVo.getOpenId());
        }
        requestMessage.setOutTradeNo(requestVo.getOrderCode());
        requestMessage.setTotalFee(requestVo.getAmount());
        requestMessage.setTradeType(requestVo.getTradeType());
        if (WechatPayConfig.H5_TRADE_TYPE.equals(requestVo.getTradeType())) {
            requestMessage.setSceneInfo(requestVo.getBody());
        }
        String sign = paySign(requestMessage, WechatPayConfig.getMchKey());
        requestMessage.setSign(sign);
        return requestMessage;
    }

    /**
     * 微信退费
     */
    public static WechatOrderRefundRequestMessage createWechatRefundRequestMessage(String orderSN, String paySN, double refundAmount, double totalAmount, String payunits) {
        WechatOrderRefundRequestMessage requestMessage = new WechatOrderRefundRequestMessage();
        requestMessage.setAppid(WechatPayConfig.getAppId());
        requestMessage.setMchId(WechatPayConfig.getMchId());
        requestMessage.setNonceStr(StringUtil.getRandomString(9));
        requestMessage.setOpUserId(WechatPayConfig.getMchId());
        if (StringUtil.isNotBlank(orderSN)) {
            requestMessage.setOutTradeNo(orderSN);
            requestMessage.setOutRefundNo(orderSN);
        }
        if (StringUtil.isNotBlank(paySN)) {
            requestMessage.setTransactionId(paySN);
        }
        requestMessage.setRefundFee(StringUtil.format(refundAmount));
        requestMessage.setTotalFee(StringUtil.format(totalAmount));
        String sign = paySign(requestMessage, WechatPayConfig.getMchKey());
        requestMessage.setSign(sign);
        return requestMessage;
    }

    /**
     * 微信小程序支付
     */
    public static WechatMicroPayRequestMessage createWeixinMicroPayRequestMessage(String amount, String orderNo, String authCode, String spbillIp, String description, String payunits) {
        WechatMicroPayRequestMessage requestMessage = new WechatMicroPayRequestMessage();
        requestMessage.setAppid(WechatPayConfig.getAppId());
        requestMessage.setMchId(WechatPayConfig.getMchId());
        requestMessage.setAuthCode(authCode);
        requestMessage.setBody(description);
        requestMessage.setNonceStr(StringUtil.getRandomString(9));
        requestMessage.setOutTradeNo(orderNo);
        requestMessage.setSpbillCreateIp(spbillIp);
        requestMessage.setTotalFee(amount);
        String sign = WechatUtil.paySign(requestMessage, WechatPayConfig.getMchKey());
        requestMessage.setSign(sign);
        return requestMessage;
    }

    /**
     * 微信支付单查询
     */
    public static WechatOrderQueryRequestMessage createWeixinOrderQueryRequestMessage(String orderNo, String transactionNo, String payunits) {
        WechatOrderQueryRequestMessage requestMessage = new WechatOrderQueryRequestMessage();
        requestMessage.setAppid(WechatPayConfig.getAppId());
        requestMessage.setMchId(WechatPayConfig.getMchId());
        requestMessage.setNonceStr(StringUtil.getRandomString(9));
        if (StringUtil.isNotBlank(orderNo)) {
            requestMessage.setOutTradeNo(orderNo);
        }
        if (StringUtil.isNotBlank(transactionNo)) {
            requestMessage.setTransactionId(transactionNo);
        }
        String sign = WechatUtil.paySign(requestMessage, WechatPayConfig.getMchKey());
        requestMessage.setSign(sign);
        return requestMessage;
    }

    /**
     * 微信交易账单下载
     */
    public static WechatReconciliationBillRequestMessage createWeixinReconciliationBillRequestMessage(String calender, String payunits) {
        WechatReconciliationBillRequestMessage requestMessage = new WechatReconciliationBillRequestMessage();
        requestMessage.setAppid(WechatPayConfig.getAppId());
        requestMessage.setBillDate(calender);
        requestMessage.setMchId(WechatPayConfig.getMchId());
        requestMessage.setBillType("ALL");
        requestMessage.setNonceStr(StringUtil.getRandomString(9));
        String sign = WechatUtil.paySign(requestMessage, WechatPayConfig.getMchKey());
        requestMessage.setSign(sign);
        return requestMessage;
    }

    public static String jsSdkSign(Object bean) {
        try {
            Map<String, String> sortMap = convertToSortMap(bean);
            String sign = "";
            for (String key : sortMap.keySet()) {
                if (StringUtil.isNotBlank(sortMap.get(key))) {
                    sign = sign + key + "=" + sortMap.get(key) + "&";
                }
            }
            sign = sign.substring(0, sign.length() - 1);
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            return byteToHex(crypt.digest());
        }
        catch (Exception e) {
            throw new CoreException("wechat-sign-error", e);
        }
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
