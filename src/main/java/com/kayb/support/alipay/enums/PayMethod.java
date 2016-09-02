package com.kayb.support.alipay.enums;

/**
 * Pay Method
 * @author @kaybinwong
 * @since 2016/8/25
 */
public enum PayMethod {

    /**
     * 信用支付
     */
    CREDIT_PAY("creditPay"),

    /**
     * 余额支付
     */
    DIRECT_PAY("directPay");

    private String value;

    private PayMethod(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
