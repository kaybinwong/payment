package com.kayb.support.wxpay.model.bill;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kayb.support.wxpay.model.enums.RefundChannel;
import com.kayb.support.wxpay.serializer.RefundChannelDeserializer;
import lombok.Data;
import lombok.ToString;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class CommonBill extends Bill {

    private static final long serialVersionUID = -4518299029269484159L;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 退款金额(元)
     */
    private Float refundFee;

    /**
     * 企业红包退款金额(元)
     */
    private Float enterRedPkgRefundFee;

    /**
     * 退款类型
     */
    @JsonDeserialize(using = RefundChannelDeserializer.class)
    private RefundChannel channel;

    /**
     * 退款状态
     */
    private String refundStatus;
}
