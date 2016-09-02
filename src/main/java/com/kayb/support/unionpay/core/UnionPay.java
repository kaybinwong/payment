package com.kayb.support.unionpay.core;

import com.kayb.support.unionpay.model.pay.AppPayModel;
import lombok.Data;

import java.util.Map;

/**
 * UnionPay
 * pfx -> pkcs12 -> pkcs8
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class UnionPay {

    // 字符集编码 默认"UTF-8"
    String encoding = "UTF-8";
    // 版本号
    String version = "5.0.0";
    // 签名方法 01 RSA
    String signMethod = "01";
    // 交易类型 01-消费
    String txnType="01";
    // 交易子类型 01:自助消费 02:订购 03:分期付款
    String txnSubType="01";
    // 业务类型
    String bizType = "000201";
    // 渠道类型，07-PC，08-手机
    String channelType = "08";
    // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
    String accessType = "0";
    // 交易币种
    String currencyCode = "156";
    String accType="01";
    // 商户号码
    String merId;
    // 后台通知地址
    String backUrl;
    // 请求地址
    String appRequestUrl;
    // 签名证书序列号
    String certId;
    // 商户私钥
    String appPriKey;
    String appPubKey;

    private Pays pays;
    private Verifies verifies;

    public UnionPay init(){
        pays = new Pays(this);
        verifies = new Verifies(this);
        return this;
    }

    public Map<String, String> pay(AppPayModel appPayModel) {
        return pays.appPay(appPayModel);
    }

    public boolean verifies(Map<String, Object> parameter) {
        return verifies.verifies(parameter);
    }

}
