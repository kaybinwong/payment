package com.kayb.support.wxpay.model.bill;

import com.kayb.support.wxpay.model.enums.WxpayField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class BillFields {

    /**
     * 账单公用起始字段
     */
    private static final List<String> START_FIELDS = Arrays.asList(
        WxpayField.TRADE_TIME, WxpayField.APP_ID, WxpayField.MCH_ID, WxpayField.SUB_MCH_ID,
        WxpayField.DEVICE_INFO, WxpayField.TRANSACTION_ID, WxpayField.OUT_TRADE_NO, WxpayField.OPEN_ID,
        WxpayField.TRADE_TYPE, WxpayField.TRADE_STATE, WxpayField.BANK_TYPE, WxpayField.FEE_TYPE,
        WxpayField.TOTAL_FEE, WxpayField.ENTER_RED_PKG_FEE
    );

    private static final List<String> END_FIELDS = Arrays.asList(
        WxpayField.BODY, WxpayField.DATA_PKG, WxpayField.COMMISSION_FEE, WxpayField.FEE_RATE
    );

    /**
     * 所有订单账单的字段集，顺序与微信返回数据保持一致
     */
    public static final List<String> ALL = initAllFields();

    /**
     * 退款账单的字段集合，顺序与微信返回数据保持一致
     */
    public static final List<String> REFUND = initRefundFields();

    /**
     * 成功账单的字段集合，顺序与微信返回数据保持一致
     */
    public static final List<String> SUCCESS = initSuccessFields();

    /**
     * 账单统计的字段集合，顺序与微信返回数据保持一致
     */
    public static final List<String> COUNT = Arrays.asList(
        WxpayField.TRADE_TOTAL_COUNT, WxpayField.TRADE_TOTAL_FEE,
        WxpayField.REFUND_TOTAL_FEE, WxpayField.COUPON_REFUND_TOTAL_FEE,
        WxpayField.COMMISSION_TOTAL_FEE
    );


    private BillFields(){}

    private static List<String> initAllFields() {
        List<String> all = new ArrayList<>();
        startFields(all);
        initCommonRefundFields(all);
        endFields(all);
        return all;
    }

    private static List<String> initRefundFields() {
        List<String> refund = new ArrayList<>();
        startFields(refund);
        refund.add(WxpayField.REFUND_APPLY_TIME);
        refund.add(WxpayField.REFUND_SUCCESS_TIME);
        initCommonRefundFields(refund);
        endFields(refund);
        return refund;
    }

    private static void initCommonRefundFields(List<String> fields){
        fields.add(WxpayField.REFUND_ID);
        fields.add(WxpayField.OUT_REFUND_NO);
        fields.add(WxpayField.REFUND_FEE);
        fields.add(WxpayField.ENTER_RED_PKG_REFUND_FEE);
        fields.add(WxpayField.REFUND_CHANNEL);
        fields.add(WxpayField.REFUND_STATUS);
    }

    private static List<String> initSuccessFields() {
        List<String> success = new ArrayList<>();
        startFields(success);
        endFields(success);
        return success;
    }

    private static void startFields(List<String> fields) {
        for (String f : START_FIELDS){
            fields.add(f);
        }
    }

    private static void endFields(List<String> fields) {
        for (String f : END_FIELDS){
            fields.add(f);
        }
    }
}
