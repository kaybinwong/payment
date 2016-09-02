package com.kayb.support.alipay.model.pay;

import com.kayb.annotation.Optional;
import com.kayb.support.alipay.enums.AlipayField;
import lombok.Data;

/**
 * Wap Pay Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class WapPayModel extends PayModel {

    private static final long serialVersionUID = -4046847553452516114L;

    /**
     * 商品展示网址
     * {@link AlipayField#SHOW_URL}
     */
    @Optional
    private String showUrl;

    /**
     * 手机支付宝token
     * {@link AlipayField#EXTERN_TOKEN}
     */
    @Optional
    private String externToken;

    /**
     * 航旅订单其它费用
     * {@link AlipayField#OTHER_FEE}
     */
    @Optional
    private String otherFee;

    /**
     * 航旅订单金额
     * {@link AlipayField#AIR_TICKET}
     */
    @Optional
    private String airTicket;

    /**
     * 是否发起实名校验
     * {@link AlipayField#RN_CHECK}
     */
    @Optional
    private String rnCheck;

    /**
     * 买家证件号码
     * {@link AlipayField#BUYER_CERT_NO}
     */
    @Optional
    private String buyerCertNo;

    /**
     * 买家真实姓名
     * {@link AlipayField#BUYER_REAL_NAME}
     */
    @Optional
    private String buyerRealName;

    /**
     * 收单场景
     * {@link AlipayField#SCENE}
     */
    @Optional
    private String scene;

    public WapPayModel(String outTradeNo, String orderName, String totalFee) {
        super(outTradeNo, orderName, totalFee);
    }

    @Override
    public String toString() {
        return "WapPayFields{" +
                "showUrl='" + showUrl + '\'' +
                ", externToken='" + externToken + '\'' +
                ", otherFee='" + otherFee + '\'' +
                ", airTicket='" + airTicket + '\'' +
                ", rnCheck='" + rnCheck + '\'' +
                ", buyerCertNo='" + buyerCertNo + '\'' +
                ", buyerRealName='" + buyerRealName + '\'' +
                ", scene='" + scene + '\'' +
                "} " + super.toString();
    }
}
