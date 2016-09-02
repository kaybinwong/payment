package com.kayb.support.wxpay.core;

/**
 * Wxpay Builder
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class WxpayBuilder {

    private Wxpay wxpay;

    private WxpayBuilder(){}

    public static WxpayBuilder newBuilder(String appId, String appSecret, String mchId){
        WxpayBuilder builder = new WxpayBuilder();
        builder.wxpay = new Wxpay(appId, appSecret, mchId);
        return builder;
    }

    public Wxpay build(){
        return wxpay.init();
    }
}
