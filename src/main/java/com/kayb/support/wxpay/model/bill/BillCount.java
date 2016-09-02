package com.kayb.support.wxpay.model.bill;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class BillCount implements Serializable {

    private static final long serialVersionUID = 2827339255910413151L;

    /**
     * 总交易笔数
     */
    private Integer tradeTotalCount;

    /**
     * 总交易额(元)
     */
    private Float tradeTotalFee;

    /**
     * 总退款金额(元)
     */
    private Float refundTotalFee;

    /**
     * 总代金券或立减优惠退款金额
     */
    private Float couponRefundTotalFee;

    /**
     * 手续费总金额
     */
    private String commissionTotalFee;
}
