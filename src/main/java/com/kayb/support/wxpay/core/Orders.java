package com.kayb.support.wxpay.core;

import com.kayb.support.wxpay.model.common.Coupon;
import com.kayb.support.wxpay.model.enums.WxpayField;
import com.kayb.support.wxpay.model.order.WxpayOrder;
import com.kayb.util.RandomStrs;
import com.kayb.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static com.kayb.util.Preconditions.*;

/**
 * Orders Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class Orders extends Component {

    /**
     * 查询订单
     */
    private static final String ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 关闭订单
     */
    private static final String ORDER_CLOSE = "https://api.mch.weixin.qq.com/pay/closeorder";

    Orders(Wxpay wxpay) {
        super(wxpay);
    }

    /**
     * 根据微信订单号查询订单
     * @param transactionId 微信订单号
     * @return PayOrder对象，或抛WepayException
     */
    public WxpayOrder queryByTransactionId(String transactionId){
        assertNotNullAndEmpty(transactionId, "transactionId");
        Map<String, String> queryParams = new TreeMap<>();
        put(queryParams, WxpayField.TRANSACTION_ID, transactionId);
        return doQueryOrder(queryParams);
    }

    /**
     * 根据商户订单号查询订单
     * @param outTradeNo 商户订单号
     * @return PayOrder对象，或抛WepayException
     */
    public WxpayOrder queryByOutTradeNo(String outTradeNo){
        assertNotNullAndEmpty(outTradeNo, "outTradeNo");
        Map<String, String> queryParams = new TreeMap<>();
        put(queryParams, WxpayField.OUT_TRADE_NO, outTradeNo);
        return doQueryOrder(queryParams);
    }

    private WxpayOrder doQueryOrder(Map<String, String> queryParams) {
        buildQueryParams(queryParams);
        Map<String, Object> orderData = doPost(ORDER_QUERY, queryParams);
        WxpayOrder order = JsonUtil.toBean(JsonUtil.toJson(orderData), WxpayOrder.class);
        setCoupons(order, orderData);
        return order;
    }

    private void setCoupons(WxpayOrder order, Map<String, Object> orderData) {
        if (order != null
                && order.getCouponCount() != null
                && order.getCouponCount() > 0){
            List<Coupon> coupons = new ArrayList<>();
            Coupon coupon;
            for (int couponIndex = 0; couponIndex < order.getCouponCount(); couponIndex++){
                coupon = Coupon.newCoupon(
                        (String)orderData.get(WxpayField.COUPON_BATCH_ID + "_" + couponIndex),
                        (String) orderData.get(WxpayField.COUPON_ID + "_" + couponIndex),
                        Integer.parseInt((String) orderData.get(WxpayField.COUPON_FEE + "_" + couponIndex))
                );
                coupons.add(coupon);
            }
            order.setCoupons(coupons);
        }
    }

    /**
     * 关闭订单
     * @param outTradeNo 商户订单号
     * @return 关闭成功返回true，或抛WepayException
     */
    public Boolean closeOrder(String outTradeNo){
        assertNotNullAndEmpty(outTradeNo, "outTradeNo");
        Map<String, String> closeParams = new TreeMap<>();
        put(closeParams, WxpayField.OUT_TRADE_NO, outTradeNo);
        buildCloseParams(closeParams);
        return doPost(ORDER_CLOSE, closeParams) != null;
    }

    /**
     * 构建关闭订单参数
     * @param closeParams 关闭参数
     */
    private void buildCloseParams(Map<String, String> closeParams) {
        buildConfigParams(closeParams);
        put(closeParams, WxpayField.NONCE_STR, RandomStrs.generate(16));
        buildSignParams(closeParams);
    }

    /**
     * 构建查询订单参数
     * @param queryParams 查询参数
     */
    private void buildQueryParams(Map<String, String> queryParams) {
        buildConfigParams(queryParams);
        put(queryParams, WxpayField.NONCE_STR, RandomStrs.generate(16));
        buildSignParams(queryParams);
    }
}
