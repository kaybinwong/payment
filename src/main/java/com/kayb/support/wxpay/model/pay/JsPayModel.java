package com.kayb.support.wxpay.model.pay;


import com.kayb.support.wxpay.model.enums.WxpayField;
import lombok.Data;
import lombok.ToString;

/**
 * Js(Web) Pay Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class JsPayModel extends PayModel {

    /**
     * 用户标识
     * {@link WxpayField#OPEN_ID}
     */
    private String openId;
}
