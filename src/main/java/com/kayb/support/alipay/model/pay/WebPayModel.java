package com.kayb.support.alipay.model.pay;

import com.kayb.support.alipay.enums.AlipayField;
import lombok.Data;

/**
 * Web Pay Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class WebPayModel extends PayModel {

    private static final long serialVersionUID = -1542442458795168095L;

    /**
     * 客服端IP
     * {@link AlipayField#EXTER_INVOKE_IP}
     */
    protected String exterInvokeIp;

    /**
     * 支付宝错误通知跳转
     * {@link AlipayField#ERROR_NOTIFY_URL}
     */
    protected String errorNotifyUrl;

    public WebPayModel(String outTradeNo, String orderName, String totalFee) {
        super(outTradeNo, orderName, totalFee);
    }

    @Override
    public String toString() {
        return "WebPayFields{" +
                "exterInvokeIp='" + exterInvokeIp + '\'' +
                ", errorNotifyUrl='" + errorNotifyUrl + '\'' +
                "} " + super.toString();
    }
}
