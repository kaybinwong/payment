package com.kayb.support.wxpay.core;

import com.kayb.support.wxpay.model.common.Coupon;
import com.kayb.support.wxpay.model.enums.FeeType;
import com.kayb.support.wxpay.model.enums.RefundChannel;
import com.kayb.support.wxpay.model.enums.WxpayField;
import com.kayb.support.wxpay.model.refund.RefundApplyModel;
import com.kayb.support.wxpay.model.refund.RefundApplyResponse;
import com.kayb.support.wxpay.model.refund.RefundItem;
import com.kayb.support.wxpay.model.refund.RefundQueryResponse;
import com.kayb.util.RandomStrs;
import com.kayb.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.kayb.util.Preconditions.*;

/**
 * Refund Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class Refunds extends Component {

    /**
     * 申请退款
     */
    private static final String APPLY = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 查询退款
     */
    private static final String QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

    Refunds(Wxpay wxpay) {
        super(wxpay);
    }

    /**
     * 申请退款
     * @param request 退款请求对象
     * @return RefundResponse对象，或抛WepayException
     */
    public RefundApplyResponse apply(RefundApplyModel request){
        checkApplyParams(request);
        Map<String, String> applyParams = buildApplyParams(request);
        return doHttpsPost(APPLY, applyParams, RefundApplyResponse.class);
    }

    /**
     * 通过商户订单号查询退款
     * @param outTradeNo 商户订单号
     * @return 退款查询对象，或抛WepayException
     */
    public RefundQueryResponse queryByOutTradeNo(String outTradeNo){
        Map<String, String> queryParams = buildQueryParams(WxpayField.OUT_TRADE_NO, outTradeNo);
        Map<String, Object> respData = doPost(QUERY, queryParams);
        return renderQueryResp(respData);
    }

    /**
     * 通过商户退款单号查询退款
     * @param outRefundNo 商户退款单号
     * @return 退款查询对象，或抛WepayException
     */
    public RefundQueryResponse queryByOutRefundNo(String outRefundNo){
        Map<String, String> queryParams = buildQueryParams(WxpayField.OUT_REFUND_NO, outRefundNo);
        Map<String, Object> respData = doPost(QUERY, queryParams);
        return renderQueryResp(respData);
    }

    /**
     * 通过微信订单号查询退款
     * @param transactionId 微信订单号
     * @return 退款查询对象，或抛WepayException
     */
    public RefundQueryResponse queryByTransactionId(String transactionId){
        Map<String, String> queryParams = buildQueryParams(WxpayField.TRANSACTION_ID, transactionId);
        Map<String, Object> respData = doPost(QUERY, queryParams);
        return renderQueryResp(respData);
    }

    /**
     * 通过微信退款单号查询退款
     * @param refundId 微信退款单号
     * @return 退款查询对象，或抛WepayException
     */
    public RefundQueryResponse queryByRefundId(String refundId){
        Map<String, String> queryParams = buildQueryParams(WxpayField.REFUND_ID, refundId);
        Map<String, Object> respData = doPost(QUERY, queryParams);
        return renderQueryResp(respData);
    }

    private RefundQueryResponse renderQueryResp(Map<String, Object> refundData) {
        RefundQueryResponse queryResp = new RefundQueryResponse();

        queryResp.setOutTradeNo((String)refundData.get(WxpayField.OUT_TRADE_NO));
        queryResp.setTransactionId((String)refundData.get(WxpayField.TRANSACTION_ID));
        queryResp.setTotalFee(Integer.parseInt((String)refundData.get(WxpayField.TOTAL_FEE)));
        queryResp.setCashFee(Integer.parseInt((String) refundData.get(WxpayField.CASH_FEE)));
        String feeType = (String)refundData.get(WxpayField.FEE_TYPE);
        if (!StringUtil.isEmpty(feeType)){
            queryResp.setFeeType(FeeType.from(feeType));
        }

        Integer refundCount = Integer.parseInt((String) refundData.get(WxpayField.REFUND_COUNT));

        List<RefundItem> refundItems = new ArrayList<>();
        RefundItem refundItem;
        for (int refundIndex = 0; refundIndex < refundCount; refundIndex++){
            refundItem = renderRefundItem(refundData, refundIndex);
            refundItems.add(refundItem);
        }
        queryResp.setItems(refundItems);

        return queryResp;
    }

    private RefundItem renderRefundItem(Map<String, Object> refundData, int refundItemIndex) {
        RefundItem refundItem = new RefundItem();
        refundItem.setOutRefundNo((String)refundData.get(WxpayField.OUT_REFUND_NO + "_" + refundItemIndex));
        refundItem.setRefundId((String)refundData.get(WxpayField.REFUND_ID + "_" + refundItemIndex));
        refundItem.setChannel(RefundChannel.from((String) refundData.get(WxpayField.REFUND_CHANNEL + "_" + refundItemIndex)));
        refundItem.setRefundFee(Integer.parseInt((String)refundData.get(WxpayField.REFUND_FEE + "_" + refundItemIndex)));
        Object couponRefundFee = refundData.get(WxpayField.COUPON_REFUND_FEE + "_" + refundItemIndex);
        if (couponRefundFee != null){
            refundItem.setCouponRefundFee(Integer.parseInt((String)couponRefundFee));
        }
        Object couponRefundCountObj = refundData.get(WxpayField.COUPON_REFUND_COUNT + "_" + refundItemIndex);
        if (couponRefundCountObj != null){
            Integer couponRefundCount = Integer.parseInt((String)couponRefundCountObj);
            if (couponRefundCount > 0){
                List<Coupon> couponItems = new ArrayList<>();
                Coupon couponItem;
                for (int couponItemIndex = 0; couponItemIndex < couponRefundCount; couponItemIndex++){
                    couponItem = Coupon.newCoupon(
                            (String) refundData.get(WxpayField.COUPON_REFUND_BATCH_ID + "_" + refundItemIndex + "_" + couponItemIndex),
                            (String) refundData.get(WxpayField.COUPON_REFUND_ID + "_" + refundItemIndex + "_" + couponItemIndex),
                            Integer.parseInt((String) refundData.get(WxpayField.COUPON_REFUND_FEE + "_" + refundItemIndex + "_" + couponItemIndex))
                    );
                    couponItems.add(couponItem);
                }
                refundItem.setCoupons(couponItems);
            }
        }

        return refundItem;
    }

    /**
     * 构建查询退款参数
     * @param queryFieldName 查询字段名
     * @param queryFieldValue 查询字段值
     * @return 查询参数
     */
    private Map<String, String> buildQueryParams(String queryFieldName, String queryFieldValue) {
        assertNotNullAndEmpty(queryFieldValue, queryFieldName);

        Map<String, String> queryParams = new TreeMap<>();
        buildConfigParams(queryParams);
        queryParams.put(WxpayField.NONCE_STR, RandomStrs.generate(16));
        queryParams.put(queryFieldName, queryFieldValue);
        buildSignParams(queryParams);

        return queryParams;
    }

    /**
     * 校验退款参数
     * @param request 退款请求对象
     */
    private void checkApplyParams(RefundApplyModel request) {
//        assertNotNull(wxpay.certs, "merchant certs can't be null before apply refund");
//        assertNotNullAndEmpty(wxpay.getCertPasswd(), "certPasswd");
        assertNotNull(request, "apply request can't be null");
        if (StringUtil.isEmpty(request.getTransactionId())){
            assertNotNullAndEmpty(request.getOutTradeNo(), "transactionId && outTradeNo");
        }
        assertNotNullAndEmpty(request.getOutRefundNo(), "outRefundNo");
        assertNotNullAndEmpty(request.getOpUserId(), "opUserId");
        Integer totalFee = request.getTotalFee();
        Integer refundFee = request.getRefundFee();
        assertPositive(totalFee, "totalFee");
        assertPositive(refundFee, "refundFee");
        assertPositive(totalFee - refundFee, "totalFee - refundFee");
    }

    /**
     * 构建退款参数
     * @param request 退款请求
     * @return 退款参数
     */
    private Map<String, String> buildApplyParams(RefundApplyModel request) {
        Map<String, String> refundParams = new TreeMap<>();

        // 配置参数
        buildConfigParams(refundParams);

        // 业务参数
        putIfNotEmpty(refundParams, WxpayField.TRANSACTION_ID, request.getTransactionId());
        putIfNotEmpty(refundParams, WxpayField.OUT_TRADE_NO, request.getOutTradeNo());
        put(refundParams, WxpayField.OUT_REFUND_NO, request.getOutRefundNo());
        put(refundParams, WxpayField.TOTAL_FEE, request.getTotalFee() + "");
        put(refundParams, WxpayField.REFUND_FEE, request.getRefundFee() + "");
        put(refundParams, WxpayField.NONCE_STR, RandomStrs.generate(16));
        put(refundParams, WxpayField.OP_USER_ID, request.getOpUserId());
        putIfNotEmpty(refundParams, WxpayField.DEVICE_INFO, request.getDeviceInfo());
        if (request.getRefundFeeType() != null){
            put(refundParams, WxpayField.REFUND_FEE_TYPE, request.getRefundFeeType().type());
        }

        // 签名参数
        buildSignParams(refundParams);

        return refundParams;
    }
}
