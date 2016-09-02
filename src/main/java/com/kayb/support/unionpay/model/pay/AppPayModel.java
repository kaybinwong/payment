package com.kayb.support.unionpay.model.pay;

import lombok.Data;

import java.math.BigDecimal;

/**
 * App Pay Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class AppPayModel {

    /**
     * 商户订单号，8-40位数字字母
     */
    private String orderId;
    private BigDecimal orderAmt;
    private Long orderTime;
    /**
     * 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
     */
    private String reqReserved;
    /**
     * 订单描述，可不上送，上送时控件中会显示该信息
     */
    private String orderDesc;

}
