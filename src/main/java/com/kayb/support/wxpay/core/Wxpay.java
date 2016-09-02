package com.kayb.support.wxpay.core;

import com.kayb.support.wxpay.exception.WxpayException;
import com.kayb.support.wxpay.model.pay.AppPayResponse;
import com.kayb.support.wxpay.model.pay.PayModel;
import com.kayb.util.StringUtil;
import lombok.Data;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;


/**
 * Wxpay
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class Wxpay {

    /**
     * 微信APP ID
     */
    String appId;

    /**
     * 微信APP Key
     */
    String appKey;

    /**
     * 商户ID
     */
    String mchId;

    /**
     * 商户证书数据(p12)
     */
    String cert;

    /**
     * 商户证书密码，默认为商户号mchId
     */
    String certPasswd;

    /**
     * 通知地址
     */
    String notifyUrl;

    private SSLSocketFactory sslSocketFactory;

    /**
     * 支付组件
     */
    private Pays pays;

    /**
     * 订单组件
     */
    private Orders orders;

    /**
     * 退款组件
     */
    private Refunds refunds;

    /**
     * 通知组件
     */
    private Notifies notifies;

    /**
     * 账单组件
     */
    private Bills bills;

    public Wxpay() {

    }

    public Wxpay(String appId, String appKey, String mchId){
        this.appId = appId;
        this.appKey = appKey;
        this.mchId = mchId;
    }

    /**
     * 初始化操作
     * @return this
     */
    public Wxpay init(){
        pays = new Pays(this);
        orders = new Orders(this);
        refunds = new Refunds(this);
        notifies = new Notifies(this);
        bills = new Bills(this);
        //初始化服务器证书
        if (!StringUtil.isEmpty(cert) && !StringUtil.isEmpty(certPasswd)){
            //sslSocketFactory = initSSLSocketFactory();
        }
        return this;
    }

    private SSLSocketFactory initSSLSocketFactory() {
        try (InputStream certsInput = new ByteArrayInputStream(cert.getBytes())){

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(certsInput, certPasswd.toCharArray());
            certsInput.close();

            keyManagerFactory.init(keyStore, certPasswd.toCharArray());
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            return  context.getSocketFactory();
        } catch (Exception e) {
            throw new WxpayException(e);
        }
    }

    public AppPayResponse appPay(PayModel payModel) {
        return pays.appPay(payModel);
    }

    public Boolean verifies(Map<String, ?> params) {
        return notifies.verifySign(params);
    }

    public String ok() {
        return notifies.ok();
    }

}
