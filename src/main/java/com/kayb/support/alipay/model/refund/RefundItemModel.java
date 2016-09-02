package com.kayb.support.alipay.model.refund;

import lombok.Data;

import java.io.Serializable;

/**
 * Refund Item Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class RefundItemModel implements Serializable {

    private static final long serialVersionUID = -9178222896185599335L;

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 退款金额(元)
     */
    private String fee;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 格式化为支付宝需要的格式
     * @return tradeNo^fee^reason
     */
    public String format(){
        return tradeNo + "^" + fee + "^" + reason;
    }

    @Override
    public String toString() {
        return "RefundItemModel{" +
                "tradeNo='" + tradeNo + '\'' +
                ", fee='" + fee + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
