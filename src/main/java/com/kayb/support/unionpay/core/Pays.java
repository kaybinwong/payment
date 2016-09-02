package com.kayb.support.unionpay.core;

import com.kayb.support.unionpay.enums.UnionPayField;
import com.kayb.support.unionpay.model.pay.AppPayModel;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Pay Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Slf4j
public class Pays extends Component {
    protected Pays(UnionPay unionPay) {
        super(unionPay);
    }

    /**
     * 手机支付
     * @param appPayModel
     * @return
     */
    public Map<String, String> appPay(AppPayModel appPayModel) {

        Map<String, String> data = new HashMap<>();
        /***银联全渠道系统***/
        data.put(UnionPayField.VERSION, unionPay.version);
        data.put(UnionPayField.ENCODING, unionPay.encoding);
        data.put(UnionPayField.SIGN_METHOD, unionPay.signMethod);
        data.put(UnionPayField.TXN_TYPE, unionPay.txnType);
        data.put(UnionPayField.TXN_SUB_TYPE, unionPay.txnSubType);
        data.put(UnionPayField.BIZ_TYPE, unionPay.bizType);
        data.put(UnionPayField.CHANNEL_TYPE, unionPay.channelType);

        /***商户接入参数***/
        data.put(UnionPayField.MER_ID, unionPay.merId);
        data.put(UnionPayField.ACCESS_TYPE, unionPay.accessType);
        data.put(UnionPayField.ORDER_ID, appPayModel.getOrderId());
        data.put(UnionPayField.TXN_TIME, new SimpleDateFormat("YYYYMMddHHmmss").format(new Date(appPayModel.getOrderTime())));
        data.put(UnionPayField.TXN_AMT, String.valueOf((appPayModel.getOrderAmt().multiply(new BigDecimal(100))).intValue()));
        data.put(UnionPayField.CURRENCY_CODE, unionPay.currencyCode);
//        data.put(UnionPayField.ACC_TYPE, unionPay.accType);
//        data.put(UnionPayField.REQ_RESERVED, appPayModel.getReqReserved());
//        data.put(UnionPayField.ORDER_DESC, appPayModel.getOrderDesc());
        data.put(UnionPayField.BACK_URL, unionPay.backUrl);

        Map<String, String> res = doPost(data, unionPay.appRequestUrl);

        log.debug("app pay =>{}", data.toString());
        log.debug("app pay <={}", res.toString());

        return res;
    }

}
