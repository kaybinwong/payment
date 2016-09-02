package com.kayb.support.alipay.enums;

/**
 * Sign Type
 * @author @kaybinwong
 * @since 2016/8/25
 */
public enum SignType {

    MD5("MD5"),

    DSA("DSA"),

    RSA("RSA");

    private String value;

    private SignType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
