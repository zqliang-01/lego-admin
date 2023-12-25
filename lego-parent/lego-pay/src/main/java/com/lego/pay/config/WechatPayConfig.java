package com.lego.pay.config;


public class WechatPayConfig {

    public static final String H5_TRADE_TYPE = "MWEB";
    public static final String QR_CODE_TRADE_TYPE = "NATIVE";
    public static final String APP_TRADE_TYPE = "APP";
    public static final String JS_TRADE_TYPE = "JSAPI";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String MERCHANT_URL = "https://api.mch.weixin.qq.com";
    // 统一下单地址
    public static final String UNIFIEDORDERURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 刷卡支付地址
    public static final String MICROPAY = "https://api.mch.weixin.qq.com/pay/micropay";
    // 获取公众号内授权openId地址
    public static final String ACCESSTOKENURL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    // 获取小程序内授权openId地址
    public static final String ACCESSAPPLETTOKENURL = "https://api.weixin.qq.com/sns/jscode2session";
    // 订单查询地址
    public static final String ORDERQUERYURL = "https://api.mch.weixin.qq.com/pay/orderquery";
    // 下载对账单地址
    public static final String DOWNLOADBILLURL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // 取消交易地址
    public static final String CLOSEORDERURL = "https://api.mch.weixin.qq.com/pay/closeorder";
    // 退款地址
    public static final String REFUNDURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    // 查询退款地址
    public static final String REFUNDQUERYURL = "https://api.mch.weixin.qq.com/pay/refundquery";

    public static final String JSACCESSTOKENURL = "https://api.weixin.qq.com/cgi-bin/token";

    public static final String TICKETURL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    private static String appId = "wxb1bbd8e52403fc1e";
    private static String secret = "3414fcf81a7141db99088f0150e78ba5";
    private static String mchId = "1276440401";
    private static String mchKey = "546545786kjdr4466643hhgj75464687";
    private static String notifyUrl = "http://zqliang.cn/legosms/pay/complete";
    private static String certPath = "C:/apiclient_cert.p12";
    private static String reconciliationFilePath = "";
    private static String jsTicket = "";

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        WechatPayConfig.appId = appId;
    }

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        WechatPayConfig.secret = secret;
    }

    public static String getMchId() {
        return mchId;
    }

    public static void setMchId(String mchId) {
        WechatPayConfig.mchId = mchId;
    }

    public static String getMchKey() {
        return mchKey;
    }

    public static void setMchKey(String mchKey) {
        WechatPayConfig.mchKey = mchKey;
    }

    public static String getNotifyUrl() {
        return notifyUrl;
    }

    public static void setNotifyUrl(String notifyUrl) {
        WechatPayConfig.notifyUrl = notifyUrl;
    }

    public static String getCertPath() {
        return certPath;
    }

    public static void setCertPath(String certPath) {
        WechatPayConfig.certPath = certPath;
    }

    public static String getReconciliationFilePath() {
        return reconciliationFilePath;
    }

    public static void setReconciliationFilePath(String reconciliationFilePath) {
        WechatPayConfig.reconciliationFilePath = reconciliationFilePath;
    }

    public static String getJsTicket() {
        return jsTicket;
    }

    public static void setJsTicket(String jsTicket) {
        WechatPayConfig.jsTicket = jsTicket;
    }

}
