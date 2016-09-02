package com.kayb.support.wxpay.model.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kayb.support.wxpay.model.enums.WxpayField;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Getter
@ToString
public class JsPayResponse implements Serializable {

    private static final long serialVersionUID = 2540820967097836895L;

    /**
     * 微信APPID
     */
    private String appId;

    /**
     * 时间戳(s)
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 预支付ID串，如: prepare_id=xxx
     * 注意: JS前端调用时的字段名为package，与java关键字冲突
     */
    @JsonProperty(WxpayField.PKG)
    private String pkg;

    /**
     * 签名类型MD5
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;

    public JsPayResponse(String appId, String timeStamp, String nonceStr, String pkg, String signType, String paySign) {
        this.appId = appId;
        this.timeStamp = timeStamp;
        this.nonceStr = nonceStr;
        this.pkg = pkg;
        this.signType = signType;
        this.paySign = paySign;
    }
}
