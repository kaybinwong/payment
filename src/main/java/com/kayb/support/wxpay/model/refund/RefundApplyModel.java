package com.kayb.support.wxpay.model.refund;

import com.kayb.annotation.Optional;
import com.kayb.support.wxpay.model.enums.FeeType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class RefundApplyModel implements Serializable {

    private static final long serialVersionUID = 5046932866574485686L;

    /**
     * 设备号
     */
    @Optional
    private String deviceInfo;

    /**
     * 微信订单号，与outTradeNo二选一
     */
    private String transactionId;

    /**
     * 商户订单号，与transactionId二选一
     */
    private String outTradeNo;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 订单总金额
     */
    private Integer totalFee;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 货币类型
     */
    @Optional
    private FeeType refundFeeType = FeeType.CNY;

    /**
     * 操作员
     */
    private String opUserId;
}
