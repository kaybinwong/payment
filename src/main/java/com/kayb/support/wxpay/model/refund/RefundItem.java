package com.kayb.support.wxpay.model.refund;

import com.kayb.support.wxpay.model.common.Coupon;
import com.kayb.support.wxpay.model.enums.RefundChannel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class RefundItem implements Serializable {

    private static final long serialVersionUID = -8803509387441693049L;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 退款渠道
     */
    private RefundChannel channel;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 代金券或立减优惠退款金额
     */
    private Integer couponRefundFee;

    /**
     * 代金券或立减优惠退款项
     */
    private List<Coupon> coupons;
}
