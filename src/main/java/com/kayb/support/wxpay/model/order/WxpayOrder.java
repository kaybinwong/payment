package com.kayb.support.wxpay.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kayb.support.wxpay.model.common.Coupon;
import com.kayb.support.wxpay.model.enums.FeeType;
import com.kayb.support.wxpay.model.enums.TradeState;
import com.kayb.support.wxpay.model.enums.TradeType;
import com.kayb.support.wxpay.serializer.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Order Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class WxpayOrder implements Serializable {

    private static final long serialVersionUID = -3808893700552515824L;

    /**
     * 用户openId
     */
    private String openId;

    /**
     * 用户是否关注公众号
     */
    @JsonDeserialize(using = BooleanDeserializer.class)
    private Boolean subscribe;

    /**
     * 交易类型
     */
    @JsonDeserialize(using = TradeTypeDeserializer.class)
    private TradeType tradeType;

    /**
     * 银行类型
     */
    private String bankType;

    /**
     * 总金额
     */
    private Integer totalFee;

    @JsonDeserialize(using = FeeTypeDeserializer.class)
    private FeeType feeType;

    /**
     * 微信订单好
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    @JsonDeserialize(using = DateDeserializer.class)
    private Date timeEnd;

    /**
     * 交易状态
     */
    @JsonDeserialize(using = TradeStateDeserializer.class)
    private TradeState tradeState;

    /**
     * 交易状态描述
     */
    private String tradeStateDesc;

    /**
     * 现金支付金额
     */
    private Integer cachFee;

    @JsonDeserialize(using = FeeTypeDeserializer.class)
    private FeeType cashFeeType;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 代金券或立减优惠金额(分)
     */
    private Integer couponFee;

    /**
     * 代金券或立减优惠使用数量
     */
    private Integer couponCount;

    /**
     * 代金券或立减优惠明细
     */
    @JsonIgnore
    private List<Coupon> coupons;
}
