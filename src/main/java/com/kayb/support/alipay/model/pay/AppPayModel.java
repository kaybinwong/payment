package com.kayb.support.alipay.model.pay;

import com.kayb.annotation.Optional;
import com.kayb.support.alipay.enums.AlipayField;
import com.kayb.support.alipay.enums.GoodsType;
import lombok.Data;

/**
 * App Pay Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class AppPayModel extends PayModel {

    private static final long serialVersionUID = 7265488308580697604L;

    /**
     * 客户端号，标识客户端
     * {@link AlipayField#APP_ID}
     */
    @Optional
    private String appId;

    /**
     * 客户端来源
     *  {@link AlipayField#APPENV}
     */
    @Optional
    private String appenv;

    /**
     * 是否发起实名校验
     * {@link AlipayField#RN_CHECK}
     */
    @Optional
    private String rnCheck;

    /**
     * 授权令牌(32)
     * {@link AlipayField#EXTERN_TOKEN}
     */
    @Optional
    private String externToken;

    /**
     * 商户业务扩展参数
     * {@link AlipayField#OUT_CONTEXT}
     */
    @Optional
    private String outContext;

    /**
     * 商品详情
     */
    private String body;

    /**
     * 商品类型
     * @see GoodsType
     */
    @Optional
    private GoodsType goodsType;

    public AppPayModel(String outTradeNo, String orderName, String totalFee, String body) {
        super(outTradeNo, orderName, totalFee);
        this.body = body;
    }

    @Override
    public String toString() {
        return "AppPayModel{" +
                "appId='" + appId + '\'' +
                ", appenv='" + appenv + '\'' +
                ", rnCheck='" + rnCheck + '\'' +
                ", externToken='" + externToken + '\'' +
                ", outContext='" + outContext + '\'' +
                ", body='" + body + '\'' +
                ", goodsType=" + goodsType +
                "} " + super.toString();
    }
}
