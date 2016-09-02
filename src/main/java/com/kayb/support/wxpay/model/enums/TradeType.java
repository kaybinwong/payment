package com.kayb.support.wxpay.model.enums;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public enum TradeType {

    /**
     * 公众号支付
     */
    JSAPI("JSAPI"),

    /**
     * 原生扫码支付
     */
    NATIVE("NATIVE"),

    /**
     * APP支付
     */
    APP("APP"),

    /**
     * 刷卡支付
     */
    MICROPAY("MICROPAY");


    private String type;

    private TradeType(String type){
        this.type = type;
    }

    public String type(){
        return type;
    }

    public static TradeType from(String s){
        for (TradeType tt : TradeType.values()){
            if (tt.type().equals(s)){
                return tt;
            }
        }
        throw new IllegalArgumentException("unknown trade type: " + s);
    }
}
