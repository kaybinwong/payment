package com.kayb.support.wxpay.model.pay;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Getter
@ToString
public class AppPayResponse implements Serializable {

    private static final long serialVersionUID = 2540820967097836895L;

    /**
     * 微信APPID
     */
    private String appId;

    /**
     * 商户Id
     */
    private String partnerId;

    /**
     * 预支付ID
     */
    private String prepayId;

    /**
     * 时间戳(s)
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * package
     */
    private String pkg = "Sign=WXPay";

    /**
     * 签名
     */
    private String paySign;

    public AppPayResponse(String appId, String partnerId, String prepayId, String timeStamp, String nonceStr, String paySign) {
        this.appId = appId;
        this.partnerId = partnerId;
        this.prepayId = prepayId;
        this.timeStamp = timeStamp;
        this.nonceStr = nonceStr;
        this.paySign = paySign;
    }
}
