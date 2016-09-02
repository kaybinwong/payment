package com.kayb.support.wxpay.model.refund;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kayb.annotation.Optional;
import com.kayb.support.wxpay.model.enums.FeeType;
import com.kayb.support.wxpay.model.enums.RefundChannel;
import com.kayb.support.wxpay.serializer.FeeTypeDeserializer;
import com.kayb.support.wxpay.serializer.RefundChannelDeserializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class RefundApplyResponse implements Serializable {

    private static final long serialVersionUID = -8303581191923588820L;

    /**
     * 设备号
     */
    @Optional
    private String deviceInfo;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 微信退款但号
     */
    private String refundId;

    /**
     * 退款渠道
     */
    @JsonDeserialize(using = RefundChannelDeserializer.class)
    private RefundChannel channel;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 订单总金额
     */
    private Integer totalFee;

    /**
     * 货币类型
     */
    @JsonDeserialize(using = FeeTypeDeserializer.class)
    private FeeType feeType;

    /**
     * 现金支付金额
     */
    private Integer cashFee;

    /**
     * 现金退款金额
     */
    private Integer cashRefundFee;

    /**
     * 代金券或立减优惠退款金额
     */
    private Integer couponRefundFee;

    /**
     * 代金券或立减优惠使用数量
     */
    private Integer couponRefundCount;

    /**
     * 代金券或立减优惠ID
     */
    private Integer couponRefundId;
}
