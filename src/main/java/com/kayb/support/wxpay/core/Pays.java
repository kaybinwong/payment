package com.kayb.support.wxpay.core;

import com.kayb.support.wxpay.exception.WxpayException;
import com.kayb.support.wxpay.model.enums.TradeType;
import com.kayb.support.wxpay.model.enums.WxpayField;
import com.kayb.support.wxpay.model.pay.*;
import com.kayb.util.RandomStrs;
import com.kayb.util.MD5Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import static com.kayb.util.Preconditions.*;

/**
 * Pay Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class Pays extends Component {

    /**
     * 统一下单接口
     */
    private static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 联图二维码转换
     */
    private static final String LIANTU_URL = "http://qr.liantu.com/api.php?text=";

    protected Pays(Wxpay wxpay) {
        super(wxpay);
    }

    /**
     * JS支付(公众号支付)
     * @param request 支付请求对象
     * @return JsPayResponse对象，或抛WepayException
     */
    public JsPayResponse jsPay(JsPayModel request){
        checkJsPayParams(request);
        Map<String, Object> respData = doJsPay(request, TradeType.JSAPI);
        return buildJsPayResp(respData);
    }

    /**
     * 动态二维码支付(NATIVE)
     * @param request 支付请求对象
     * @return 使用联图生成的二维码链接，或抛WepayException
     */
    public String qrPay(QrPayModel request){
        return qrPay(request, Boolean.TRUE);
    }

    /**
     * 动态二维码支付(NATIVE)
     * @param request 支付请求对象
     * @param convert 是否转换为二维码图片链接(使用联图)
     * @return 可访问的二维码链接，或抛WepayException
     */
    public String qrPay(QrPayModel request, Boolean convert){
        checkPayParams(request);
        Map<String, Object> respData = doQrPay(request, TradeType.NATIVE);
        String codeUrl = String.valueOf(respData.get(WxpayField.CODE_URL));
        if (convert){
            try {
                return LIANTU_URL + URLEncoder.encode(codeUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new WxpayException(e);
            }
        }
        return codeUrl;
    }

    /**
     * app支付
     * @param request 支付请求对象
     * @return AppPayResponse对象
     */
    public AppPayResponse appPay(PayModel request){
        checkPayParams(request);
        Map<String, Object> respData = doAppPay(request, TradeType.APP);
        return buildAppPayResp(respData);
    }

    /**
     * JS支付
     * @param request 支付信息
     * @return 支付结果
     */
    private Map<String, Object> doJsPay(JsPayModel request, TradeType tradeType){
        Map<String, String> payParams = buildPayParams(request, tradeType);
        payParams.put(WxpayField.OPEN_ID, request.getOpenId());
        return doPay(payParams);
    }

    /**
     * APP支付
     * @param request 支付信
     * @return 支付结
     */
    private Map<String, Object> doAppPay(PayModel request, TradeType tradeType){
        Map<String, String> payParams = buildPayParams(request, tradeType);
        return doPay(payParams);
    }

    /**
     * 二维码支付
     * @param request 支付信息
     * @return 支付结果
     */
    private Map<String, Object> doQrPay(QrPayModel request, TradeType tradeType){
        Map<String, String> payParams = buildPayParams(request, tradeType);
        putIfNotEmpty(payParams, WxpayField.PRODUCT_ID, request.getProductId());
        return doPay(payParams);
    }

    private Map<String, Object> doPay(Map<String, String> payParams) {
        buildSignParams(payParams);
        return doPost(PAY_URL, payParams);
    }

    private JsPayResponse buildJsPayResp(Map<String, Object> data) {

        String appId = wxpay.getAppId();
        String nonceStr = RandomStrs.generate(16);
        String timeStamp = String.valueOf(new Date().getTime() / 1000);
        String pkg = WxpayField.PREPAY_ID + "=" +
                data.get(WxpayField.PREPAY_ID);

        String signing =
                WxpayField.APPID + "=" + appId +
                "&"+ WxpayField.NONCESTR2 +"=" + nonceStr +
                "&" + WxpayField.PKG + "=" + pkg +
                "&" + WxpayField.SIGN_TYPE + "=MD5" +
                "&" + WxpayField.TIME_STAMP + "=" + timeStamp +
                "&" + WxpayField.KEY + "=" + wxpay.getAppKey();

        String signed = MD5Util.generate(signing).toUpperCase();

        return new JsPayResponse(appId, timeStamp, nonceStr, pkg, "MD5", signed);
    }

    private AppPayResponse buildAppPayResp(Map<String, Object> data) {
        String appId = wxpay.getAppId();
        String partnerId= wxpay.getMchId();
        String nonceStr = RandomStrs.generate(16);
        String timeStamp = String.valueOf(new Date().getTime() / 1000);
        String prepayId = String.valueOf(data.get(WxpayField.PREPAY_ID));

        String signing =
                WxpayField.APP_ID + "=" + appId +
                "&"+ WxpayField.NONCESTR +"=" + nonceStr +
                "&" + WxpayField.PKG + "=Sign=WXPay" +
                "&" + WxpayField.PARTNERID + "=" + partnerId +
                "&" + WxpayField.PREPAYID + "=" + prepayId +
                "&" + WxpayField.TIMESTAMP + "=" + timeStamp +
                "&" + WxpayField.KEY + "=" + wxpay.getAppKey();

        String signed = MD5Util.generate(signing).toUpperCase();

        return new AppPayResponse(appId, partnerId, prepayId, timeStamp, nonceStr, signed);
    }

    /**
     * 检查支付参数合法性
     * @param request 支付请求对象
     */
    private void checkJsPayParams(JsPayModel request) {
        checkPayParams(request);
        assertNotNullAndEmpty(request.getOpenId(), "openId");
    }

    private void checkPayParams(PayModel request) {
        assertNotNull(request, "pay detail can't be null");
        assertNotNullAndEmpty(request.getBody(), "body");
        assertNotNullAndEmpty(request.getOutTradeNo(), "outTradeNo");
        assertPositive(request.getTotalFee(), "totalFee");
        assertNotNullAndEmpty(request.getClientId(), "clientId");
        assertNotNullAndEmpty(wxpay.notifyUrl, "notifyUrl");
        assertNotNull(request.getFeeType(), "feeType can't be null");
    }

    /**
     * 构建公共支付参数
     * @param request 支付请求对象
     * @param tradeType 交易类型
     * @return 支付MAP参数
     */
    private Map<String, String> buildPayParams(PayModel request, TradeType tradeType) {
        Map<String, String> payParams = new TreeMap<>();

        // 配置参数
        buildConfigParams(payParams);

        // 业务必需参数
        put(payParams, WxpayField.BODY, request.getBody());
        put(payParams, WxpayField.OUT_TRADE_NO, request.getOutTradeNo());
        put(payParams, WxpayField.TOTAL_FEE, String.valueOf(request.getTotalFee()));
        put(payParams, WxpayField.SPBILL_CREATE_IP, request.getClientId());
        put(payParams, WxpayField.NOTIFY_URL, wxpay.notifyUrl);
        put(payParams, WxpayField.FEE_TYPE, request.getFeeType().type());
        put(payParams, WxpayField.NONCE_STR, RandomStrs.generate(16));
        put(payParams, WxpayField.TIME_START, System.currentTimeMillis() / 1000 + "");
        put(payParams, WxpayField.TRADE_TYPE, tradeType.type());

        // 业务可选参数
        putIfNotEmpty(payParams, WxpayField.DEVICE_INFO, request.getDeviceInfo());
        putIfNotEmpty(payParams, WxpayField.ATTACH, request.getAttach());
        putIfNotEmpty(payParams, WxpayField.DETAIL, request.getDetail());
        putIfNotEmpty(payParams, WxpayField.GOODS_TAG, request.getGoodsTag());
        putIfNotEmpty(payParams, WxpayField.TIME_EXPIRE, request.getTimeExpire());
        putIfNotEmpty(payParams, WxpayField.LIMIT_PAY, request.getLimitPay());

        return payParams;
    }
}
