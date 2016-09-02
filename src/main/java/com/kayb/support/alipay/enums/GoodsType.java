package com.kayb.support.alipay.enums;


/**
 * Goods Type
 * @author @kaybinwong
 * @since 2016/8/25
 */
public enum GoodsType {

    /**
     * 虚拟物品
     */
    VIRTUAL("0"),

    /**
     * 实物
     */
    REAL("1");

    private String value;

    private GoodsType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
