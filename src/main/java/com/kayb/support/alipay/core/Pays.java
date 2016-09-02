package com.kayb.support.alipay.core;

import com.kayb.support.alipay.exception.AliPayException;
import com.kayb.support.alipay.enums.AlipayField;
import com.kayb.support.alipay.enums.Service;
import com.kayb.support.alipay.enums.SignType;
import com.kayb.support.alipay.model.pay.AppPayModel;
import com.kayb.support.alipay.model.pay.PayModel;
import com.kayb.support.alipay.model.pay.WapPayModel;
import com.kayb.support.alipay.model.pay.WebPayModel;
import com.kayb.util.Preconditions;
import com.kayb.util.RSAUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Pay Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class Pays extends Component {

    Pays(Alipay alipay){
        super(alipay);
    }

    /**
     * WEB支付
     * @param webPayModel 支付字段信息
     * @return 自动提交表单(可直接输出到浏览器)
     */
    public String webPay(WebPayModel webPayModel){
        Map<String, String> payParams = buildWebPayParams(webPayModel);
        return buildPayForm(payParams);
    }

    /**
     * 构建PC支付参数
     * @param webPayModel 字段集合
     * @return WEB支付参数
     */
    private Map<String, String> buildWebPayParams(WebPayModel webPayModel) {

        // 公共参数
        Map<String, String> webPayParams = buildPayParams(webPayModel, Service.WEB_PAY);

        // PC特有参数
        putIfNotEmpty(webPayParams, AlipayField.EXTER_INVOKE_IP, webPayModel.getExterInvokeIp());
        putIfNotEmpty(webPayParams, AlipayField.ERROR_NOTIFY_URL, webPayModel.getErrorNotifyUrl());

        // md5签名参数
        buildMd5SignParams(webPayParams);

        return webPayParams;
    }

    /**
     * WAP支付
     * @param wapPayModel 支付字段信息
     * @return 自动提交表单(可直接输出到浏览器)
     */
    public String wapPay(WapPayModel wapPayModel){
        Map<String, String> wapPayParams = buildWapPayParams(wapPayModel);
        return buildPayForm(wapPayParams);
    }

    /**
     * 构建WAP支付参数
     * @param wapPayModel 字段集合
     * @return WAP支付参数
     */
    private Map<String, String> buildWapPayParams(WapPayModel wapPayModel) {

        // 公共参数
        Map<String, String> wapPayParams = buildPayParams(wapPayModel, Service.WAP_PAY);

        // WAP特有参数
        putIfNotEmpty(wapPayParams, AlipayField.SHOW_URL, wapPayModel.getShowUrl());
        putIfNotEmpty(wapPayParams, AlipayField.RN_CHECK, wapPayModel.getRnCheck());
        putIfNotEmpty(wapPayParams, AlipayField.AIR_TICKET, wapPayModel.getAirTicket());
        putIfNotEmpty(wapPayParams, AlipayField.BUYER_CERT_NO, wapPayModel.getBuyerCertNo());
        putIfNotEmpty(wapPayParams, AlipayField.BUYER_REAL_NAME, wapPayModel.getBuyerRealName());
        putIfNotEmpty(wapPayParams, AlipayField.EXTERN_TOKEN, wapPayModel.getExternToken());
        putIfNotEmpty(wapPayParams, AlipayField.OTHER_FEE, wapPayModel.getOtherFee());
        putIfNotEmpty(wapPayParams, AlipayField.SCENE, wapPayModel.getScene());

        // md5签名参数
        buildMd5SignParams(wapPayParams);

        return wapPayParams;
    }

    /**
     * 构建支付表单
     * @param payParams 支付参数
     * @return 支付表单
     */
    private String buildPayForm(Map<String, String> payParams) {
        StringBuilder form = new StringBuilder();

        form.append("<form id=\"pay_form\" name=\"pay_form\"").append(" action=\"" + alipay.gateWay).append(AlipayField.INPUT_CHARSET+"=").append(alipay.charset).append("\" method=\"POST\">");
        for (Map.Entry<String, String> param : payParams.entrySet()){
            form.append("<input type=\"hidden\" name=\"")
                    .append(param.getKey()).append("\" value=\"").append(param.getValue()).append("\" />");
        }
        form.append("<input type=\"submit\" value=\"去支付\" style=\"display:none;\" />");
        form.append("</form>");
        form.append("<script>document.forms['pay_form'].submit();</script>");

        return form.toString();
    }

    /**
     * APP支付
     * @param appPayModel 支付字段信息
     * @return APP支付字符串
     */
    public String appPay(AppPayModel appPayModel){
        Preconditions.assertNotNullAndEmpty(alipay.appPriKey, "app private key");
        Map<String, String> appPayParams = buildAppPayParams(appPayModel);
        return buildRsaPayString(appPayParams);
    }

    private Map<String, String> buildAppPayParams(AppPayModel appPayModel) {

        // 公共参数
        Map<String, String> appPayParams = buildPayParams(appPayModel, Service.APP_PAY);
        // APP支付无return_url
        appPayParams.remove(AlipayField.RETURN_URL.field());

        // APP特有参数
        Preconditions.assertNotNullAndEmpty(appPayModel.getBody(), "body");
        appPayParams.put(AlipayField.BODY.field(), appPayModel.getBody());

        putIfNotEmpty(appPayParams, AlipayField.APP_ID, appPayModel.getAppId());
        putIfNotEmpty(appPayParams, AlipayField.APPENV, appPayModel.getAppenv());
        putIfNotEmpty(appPayParams, AlipayField.EXTERN_TOKEN, appPayModel.getExternToken());
        putIfNotEmpty(appPayParams, AlipayField.OUT_CONTEXT, appPayModel.getOutContext());
        putIfNotEmpty(appPayParams, AlipayField.RN_CHECK, appPayModel.getRnCheck());
        if (appPayModel.getGoodsType() != null){
            appPayParams.put(AlipayField.GOODS_TYPE.field(), appPayModel.getGoodsType().value());
        }

        return appPayParams;
    }

    /**
     * 构建RSA签名参数
     * @param payParams 支付参数
     * @return RSA签名后的支付字符串
     */
    private String buildRsaPayString(Map<String, String> payParams) {
        String payString = buildSignString(payParams, "\"");
        String sign = RSAUtil.sign(payString, alipay.appPriKey, alipay.charset);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AliPayException("sign encode failed", e);
        }
        payString += ("&sign_type=\"" + SignType.RSA.value() + "\"&sign=\""+ sign +"\"");
        return payString;
    }

    /**
     * 构建支付公共参数
     * @param payModel 字段
     * @param service 服务接口
     * @return PC和WAP公共支付参数
     */
    private Map<String, String> buildPayParams(PayModel payModel, Service service){

        Map<String, String> payParams = new HashMap<>();

        // 配置参数
        payParams.putAll(alipay.payConfig);

        // 业务参数
        payParams.put(AlipayField.SERVICE.field(), service.value());

        Preconditions.assertNotNullAndEmpty(payModel.getOutTradeNo(), "outTradeNo");
        payParams.put(AlipayField.OUT_TRADE_NO.field(), payModel.getOutTradeNo());

        Preconditions.assertNotNullAndEmpty(payModel.getOrderName(), "orderName");
        payParams.put(AlipayField.SUBJECT.field(), payModel.getOrderName());

        Preconditions.assertNotNullAndEmpty(payModel.getTotalFee(), "totalFee");
        payParams.put(AlipayField.TOTAL_FEE.field(), payModel.getTotalFee());

        putIfNotEmpty(payParams, AlipayField.NOTIFY_URL, alipay.notifyUrl);
        putIfNotEmpty(payParams, AlipayField.RETURN_URL, payModel.getReturnUrl());

        return payParams;
    }
}
