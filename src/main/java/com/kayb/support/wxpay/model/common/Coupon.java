package com.kayb.support.wxpay.model.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class Coupon implements Serializable {

    private static final long serialVersionUID = -2006707624918389486L;

    /**
     * 代金券或立减优惠批次ID
     */
    private String batchId;

    /**
     * 代金券或立减优惠ID
     */
    private String id;

    /**
     * 单个代金券或立减优惠支付金额
     */
    private Integer fee;

    private Coupon(){}

    public static Coupon newCoupon(String batchId, String id, Integer fee){
        Coupon c = new Coupon();
        c.batchId = batchId;
        c.id = id;
        c.fee = fee;
        return c;
    }
}
