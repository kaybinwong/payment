package com.kayb.support.wxpay.model.bill;

import lombok.Data;
import lombok.ToString;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class RefundBill extends CommonBill {

    private static final long serialVersionUID = -6051679079124227683L;

    /**
     * 退款申请时间
     */
    private String refundApplyTime;

    /**
     * 退款成功时间
     */
    private String refundSuccessTime;
}
