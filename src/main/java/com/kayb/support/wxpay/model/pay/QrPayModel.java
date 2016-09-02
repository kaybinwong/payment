package com.kayb.support.wxpay.model.pay;

import com.kayb.annotation.Optional;
import com.kayb.support.wxpay.model.enums.WxpayField;
import lombok.Data;
import lombok.ToString;

/**
 * QR Pay Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class QrPayModel extends PayModel {

    /**
     * 商品ID
     * {@link WxpayField#PRODUCT_ID}
     */
    @Optional(any = false)
    private String productId;
}
