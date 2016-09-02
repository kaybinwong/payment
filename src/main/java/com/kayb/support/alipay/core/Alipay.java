package com.kayb.support.alipay.core;

import com.kayb.support.alipay.model.pay.AppPayModel;
import com.kayb.support.alipay.model.pay.WapPayModel;
import com.kayb.support.alipay.model.pay.WebPayModel;
import com.kayb.support.alipay.model.refund.RefundModel;
import com.kayb.support.alipay.enums.AlipayField;
import com.kayb.support.alipay.enums.PayMethod;
import com.kayb.support.alipay.enums.PaymentType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class Alipay {

    /**
     * 支付宝网关
     */
    String gateWay = "https://mapi.alipay.com/gateway.do?";

    /**
     * 签约的支付宝账号对应的支付宝唯一用户号，以2088开头的16位纯数字组成。
     */
    String merchantId;

    /**
     * 商户密钥
     */
    String secret;

    /**
     * 商户邮箱帐号
     */
    String email;

    /**
     * 商户网站使用的编码格式，如utf-8、gbk、gb2312等
     */
    String charset = "UTF-8";

    /**
     * 支付类型
     */
    String paymentType = PaymentType.BUY.value();

    /**
     * 默认支付方式
     */
    String payMethod = PayMethod.DIRECT_PAY.value();

    /**
     * 支付超时时间
     */
    String expired = "1h";

    /**
     * APP RSA私钥
     */
    String appPriKey;

    /**
     * APP RSA公钥
     */
    String appPubKey;

    /**
     * 二维码 RSA私钥
     */
    String qrPriKey;

    /**
     * 二维码 RSA公钥
     */
    String qrPubKey;

    /**
     * 支付宝后置通知url
     * {@link AlipayField#NOTIFY_URL}
     */
    String notifyUrl;

    /**
     * 支付配置参数，不需每次请求都生成
     */
    Map<String, String> payConfig;

    /**
     * 退款配置参数，不需每次请求都生成
     */
    Map<String, String> refundConfig;

    Map<String, Component> components = new HashMap<>();

    public Alipay(){

    }

    public Alipay(String merchantId, String secret){
        this.merchantId = merchantId;
        this.secret = secret;
    }

    public Alipay init() {
        initConfig();
        initComponents();
        return this;
    }

    private void initConfig() {
        payConfig = new HashMap<>();
        payConfig.put(AlipayField.PARTNER.field(), merchantId);
        payConfig.put(AlipayField.SELLER_ID.field(), merchantId);
        payConfig.put(AlipayField.PAYMENT_TYPE.field(), paymentType);
        payConfig.put(AlipayField.PAY_METHOD.field(), payMethod);
        payConfig.put(AlipayField.IT_B_PAY.field(), expired);
        payConfig.put(AlipayField.INPUT_CHARSET.field(), charset);

        refundConfig = new HashMap<>();
        refundConfig.put(AlipayField.PARTNER.field(), merchantId);
        refundConfig.put(AlipayField.INPUT_CHARSET.field(), charset);
        refundConfig.put(AlipayField.SELLER_USER_ID.field(), merchantId);

    }

    private void initComponents() {
        components.put(Pays.class.getSimpleName(), new Pays(this));
        components.put(Refunds.class.getSimpleName(), new Refunds(this));
        components.put(Verifies.class.getSimpleName(), new Verifies(this));
    }

    @Override
    public String toString() {
        return "Alipay{" +
                "merchantId='" + merchantId + '\'' +
                ", secret='" + secret + '\'' +
                ", charset='" + charset + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", expired='" + expired + '\'' +
                ", appPriKey='" + appPriKey + '\'' +
                ", appPubKey='" + appPubKey + '\'' +
                ", qrPriKey='" + qrPriKey + '\'' +
                ", qrPubKey='" + qrPubKey + '\'' +
                '}';
    }

    public String pay(WebPayModel webPayModel) {
        return ((Pays)components.get(Pays.class.getSimpleName())).webPay(webPayModel);
    }

    public String pay(WapPayModel wapPayModel) {
        return ((Pays)components.get(Pays.class.getSimpleName())).wapPay(wapPayModel);
    }

    public String pay(AppPayModel appPayModel) {
        return ((Pays)components.get(Pays.class.getSimpleName())).appPay(appPayModel);
    }

    public Boolean refund(RefundModel refundModel) {
        return ((Refunds)components.get(Refunds.class.getSimpleName())).refund(refundModel);
    }

    public Boolean verifies(Map<String, Object> parameter) {

        Map<String, String> p = new HashMap<>();
        for(Map.Entry<String, Object> entry : parameter.entrySet()) {
            p.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        Verifies verifies = ((Verifies)components.get(Verifies.class.getSimpleName()));
        return verifies.notifyId(p.get(AlipayField.NOTIFY_ID.field())) && verifies.rsa(p);
    }
}
