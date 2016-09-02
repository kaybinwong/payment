package com.kayb.support.wxpay.model.refund;

import com.kayb.support.wxpay.model.enums.FeeType;
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
public class RefundQueryResponse implements Serializable {

    private static final long serialVersionUID = -3559898607397949643L;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 总金额
     */
    private Integer totalFee;

    /**
     * 货币类型
     */
    private FeeType feeType;

    /**
     * 现金支付金额
     */
    private Integer cashFee;

    /**
     * 退款项
     */
    private List<RefundItem> items;
}
