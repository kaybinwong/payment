package com.kayb.support.wxpay.core;

import com.kayb.support.wxpay.model.enums.WxpayField;
import com.kayb.util.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Notifies Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class Notifies extends Component {

    Notifies(Wxpay wxpay) {
        super(wxpay);
    }

    /**
     * 签名校验
     * @param params 待验证参数(包含sign)
     * @return 验证通过返回true，反之false
     */
    public Boolean verifySign(Map<String, ?> params){
        return doVerifySign(params);
    }

    /**
     * 通知成功
     * @return 通知成功的XML消息
     */
    public String ok(){
        Map<String, String> notifyParams = new HashMap<>();
        notifyParams.put(WxpayField.RETURN_CODE, "SUCCESS");
        notifyParams.put(WxpayField.RETURN_MSG, "OK");
        return Maps.toXml(notifyParams);
    }

    /**
     * 通知不成功
     * @param errMsg 失败消息
     * @return 通知失败的XML消息
     */
    public String notOk(String errMsg){
        Map<String, String> notifyParams = new HashMap<>();
        notifyParams.put(WxpayField.RETURN_CODE, "FAIL");
        notifyParams.put(WxpayField.RETURN_MSG, errMsg);
        return Maps.toXml(notifyParams);
    }
}
