package com.kayb.support.alipay.enums;

/**
 * Payment Type
 * @author @kaybinwong
 * @since 2016/8/25
 */
public enum PaymentType {

    /**
     * buy the item
     */
    BUY("1"),

    /**
     * donate
     */
    DONATE("47"),

    /**
     * coupon
     */
    CARD_COUPON("47");

    private String value;

    private PaymentType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
