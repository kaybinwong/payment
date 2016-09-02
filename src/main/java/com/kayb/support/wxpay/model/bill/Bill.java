package com.kayb.support.wxpay.model.bill;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kayb.support.wxpay.model.enums.FeeType;
import com.kayb.support.wxpay.model.enums.TradeType;
import com.kayb.support.wxpay.serializer.FeeTypeDeserializer;
import com.kayb.support.wxpay.serializer.TradeTypeDeserializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class Bill implements Serializable {

    private static final long serialVersionUID = 2385619717854141289L;

    /**
     * 交易时间
     */
    private String tradeTime;

    /**
     * 公众帐号ID
     */
    private String appId;

    /**
     * 商户ID
     */
    private String mchId;

    /**
     * 子商户ID
     */
    private String subMchId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 用户标识
     */
    private String openId;

    /**
     * 交易类型
     */
    @JsonDeserialize(using = TradeTypeDeserializer.class)
    private TradeType tradeType;

    /**
     * 交易状态
     */
    private String tradeState;

    /**
     * 付款银行
     */
    private String bankType;

    /**
     * 货币类型
     */
    @JsonDeserialize(using = FeeTypeDeserializer.class)
    private FeeType feeType;

    /**
     * 总金额
     */
    private Float totalFee;

    /**
     * 企业红包金额
     */
    private Float enterRedPkgFee;

    /**
     * 商品名称
     */
    private String body;

    /**
     * 商户数据包
     */
    private String dataPkg;

    /**
     * 手续费
     */
    private String commissionFee;

    /**
     * 费率
     */
    private String feeRate;
}


