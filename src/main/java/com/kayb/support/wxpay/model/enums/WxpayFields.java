package com.kayb.support.wxpay.model.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class WxpayFields {

    public static List<String> APP_PAY_NOTIFY = Arrays.asList(
            WxpayField.APP_ID, WxpayField.MCH_ID, WxpayField.DEVICE_INFO, WxpayField.NONCE_STR,
            WxpayField.SIGN, WxpayField.RESULT_CODE, WxpayField.ERR_CODE, WxpayField.ERR_CODE_DES,
            WxpayField.OPEN_ID, WxpayField.IS_SUBSCRIBE, WxpayField.TRADE_TYPE, WxpayField.BANK_TYPE,
            WxpayField.TOTAL_FEE, WxpayField.FEE_TYPE, WxpayField.CASH_FEE, WxpayField.CASH_FEE_TYPE,
            WxpayField.COUPON_FEE, WxpayField.COUPON_COUNT,
            WxpayField.TRANSACTION_ID, WxpayField.OUT_TRADE_NO, WxpayField.ATTACH, WxpayField.TIME_START
    );

}
