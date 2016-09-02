package com.kayb.support.alipay.model.pay;

import com.kayb.annotation.Optional;
import com.kayb.support.alipay.enums.AlipayField;
import lombok.Data;

import java.io.Serializable;

/**
 * Pay Common Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class PayModel implements Serializable {

    private static final long serialVersionUID = 5892926888312847503L;

    /**
     * 我方唯一订单号
     * {@link AlipayField#OUT_TRADE_NO}
     */
    protected String outTradeNo;

    /**
     * 商品名称
     * {@link AlipayField#ORDER_NAME}
     */
    protected String orderName;

    /**
     * 商品金额(元)
     * {@link AlipayField#TOTAL_FEE}
     */
    protected String totalFee;

    /**
     * 支付宝前端跳转url，若为空，则使用Alipay类中的returnUrl
     * {@link AlipayField#RETURN_URL}
     */
    @Optional
    protected String returnUrl;

    public PayModel(String outTradeNo, String orderName, String totalFee) {
        this.outTradeNo = outTradeNo;
        this.orderName = orderName;
        this.totalFee = totalFee;
    }

    @Override
    public String toString() {
        return "PayFields{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", orderName='" + orderName + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                '}';
    }
}
