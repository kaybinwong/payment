package com.kayb.support.alipay.core;

import com.kayb.support.alipay.enums.AlipayField;
import com.kayb.support.alipay.enums.Service;
import com.kayb.util.OkHttp;
import com.kayb.util.Preconditions;
import com.kayb.util.RSAUtil;

import java.util.Map;
import java.util.Objects;
import static com.kayb.util.Preconditions.*;

/**
 * Verifies Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class Verifies extends Component {

    Verifies(Alipay alipay){
        super(alipay);
    }

    /**
     * MD5验证通知参数签名是否合法(WEB，WAP，退款服务器通知)
     * @param notifyParams 通知参数
     * @return 合法返回true，反之false
     */
    public Boolean md5(Map<String, String> notifyParams){
        Map<String, String> validParams = filterSigningParams(notifyParams);
        String signing = buildSignString(validParams);
        String signed = md5(signing);
        return Objects.equals(notifyParams.get(AlipayField.SIGN.field()), signed);
    }

    /**
     * RSA验证通知参数是否合法(如APP服务器通知)
     * @param notifyParams 通知参数
     * @return 合法返回true，反之false
     */
    public Boolean rsa(Map<String, String> notifyParams){
        String expectSign = notifyParams.get(AlipayField.SIGN.field());
        Map<String, String> validParams = filterSigningParams(notifyParams);
        String signing = buildSignString(validParams);
        Preconditions.assertNotNullAndEmpty(alipay.appPubKey, "app public key can't be empty before rsa verify.");
        return RSAUtil.verify(signing, expectSign, alipay.appPubKey, alipay.charset);
    }

    /**
     * 验证支付通知ID是否合法
     * @param notifyId 通知ID，后置通知中会有
     * @return 合法返回true，反之false
     */
    public Boolean notifyId(String notifyId){
        String url = alipay.gateWay +
                "&" + AlipayField.SERVICE.field() + "=" + Service.NOTIFY_VERIFY.value() +
                "&" + AlipayField.PARTNER.field() + "=" + alipay.merchantId +
                "&" + AlipayField.NOTIFY_ID.field() + "=" + notifyId;
        log.info("notify url check->>{}", url);
        String resp = OkHttp.get(url).toString();
        log.info("notify url check rsp->>{}", resp);
        return "true".equalsIgnoreCase(resp);
    }
}
