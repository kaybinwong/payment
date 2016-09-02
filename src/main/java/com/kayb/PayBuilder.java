package com.kayb;

import com.kayb.support.alipay.core.AlipayBuilder;
import com.kayb.support.wxpay.core.WxpayBuilder;

/**
 * Pay Builder
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class PayBuilder {

    public static AlipayBuilder aliBuilder(String merchantId, String secret) {
        return AlipayBuilder.newBuilder(merchantId, secret);
    }

    public static WxpayBuilder wxpayBuilder(String appId, String appSecret, String mchId) {
        return WxpayBuilder.newBuilder(appId, appSecret, mchId);
    }

}
