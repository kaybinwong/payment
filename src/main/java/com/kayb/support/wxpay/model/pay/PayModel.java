package com.kayb.support.wxpay.model.pay;

import com.kayb.annotation.Optional;
import com.kayb.support.wxpay.model.enums.FeeType;
import com.kayb.support.wxpay.model.enums.WxpayField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class PayModel implements Serializable {

    /**
     * 商品描述
     * {@link WxpayField#BODY}
     */
    private String body;

    /**
     * 业务系统唯一订单号
     * {@link WxpayField#OUT_TRADE_NO}
     */
    private String outTradeNo;

    /**
     * 总金额(分)
     * {@link WxpayField#TOTAL_FEE}
     */
    private Integer totalFee;

    /**
     * 客户端IP
     * {@link WxpayField#SPBILL_CREATE_IP}
     */
    private String clientId;

    /**
     * 设备号
     * {@link WxpayField#DEVICE_INFO}
     */
    @Optional
    private String deviceInfo;

    /**
     * 附加信息
     * {@link WxpayField#ATTACH}
     */
    @Optional
    private String attach;

    /**
     * 商品详情
     * {@link WxpayField#DETAIL}
     */
    @Optional
    private String detail;

    /**
     * 货币类型
     * {@link WxpayField#FEE_TYPE}
     */
    private FeeType feeType = FeeType.CNY;

    /**
     * 交易结束时间
     * {@link WxpayField#TIME_EXPIRE}
     */
    @Optional
    private String timeExpire;

    /**
     * 商品标记
     * {@link WxpayField#GOODS_TAG}
     */
    @Optional
    private String goodsTag;

    /**
     * 指定支付方式
     * {@link WxpayField#LIMIT_PAY}
     */
    @Optional
    private String limitPay;
}
