package com.kayb.support.alipay.core;

import com.kayb.support.alipay.exception.AliPayException;
import com.kayb.support.alipay.enums.AlipayField;
import com.kayb.support.alipay.enums.Service;
import com.kayb.support.alipay.model.refund.RefundModel;
import com.kayb.support.alipay.model.refund.RefundItemModel;
import com.kayb.util.DateUtil;
import com.kayb.util.OkHttp;
import com.kayb.util.Preconditions;
import com.kayb.util.StringUtil;
import com.kayb.util.xml.XmlReaders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kayb.util.Preconditions.*;

/**
 * Refund Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class Refunds extends Component {

    Refunds(Alipay alipay){
        super(alipay);
    }

    /**
     * 发起退款请求
     * @param refundModel 退款明细
     * @return 退款是否提交成功(不表示实际退款是否, 需从支付宝退款通知中来确认)
     */
    public Boolean refund(RefundModel refundModel){
        try {
            String url = alipay.gateWay + "_input_charset=" + alipay.charset;
            Map<String, String> refundParams = buildRefundParams(refundModel);
            String resp = OkHttp.get(url).addParams(refundParams).toString();
            XmlReaders reader = XmlReaders.create(resp);
            String isSuccess = reader.getNodeStr("is_success");
            if ("T".equals(isSuccess)){
                return Boolean.TRUE;
            }
        } catch (Exception e){
            throw new AliPayException(e);
        }
        return Boolean.FALSE;
    }

    private Map<String, String> buildRefundParams(RefundModel refundModel) {

        Map<String, String> refundParams = new HashMap<>();

        // 配置参数
        refundParams.putAll(alipay.refundConfig);

        // 接口名
        refundParams.put(AlipayField.SERVICE.field(), Service.REFUND_NO_PWD.value());

        if (!StringUtil.isEmpty(alipay.email)){
            refundParams.put(AlipayField.SELLER_EMAIL.field(), alipay.email);
        }

        // 通知URL
        if (!StringUtil.isEmpty(refundModel.getNotifyUrl())){
            refundParams.put(AlipayField.NOTIFY_URL.field(), refundModel.getNotifyUrl());
        }

        // 卖家ID
        refundParams.put(AlipayField.SELLER_USER_ID.field(), alipay.merchantId);

        // 退款日期
        refundParams.put(AlipayField.REFUND_DATE.field(), DateUtil.dateToStringByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));

        // 退款批次号
        Preconditions.assertNotNullAndEmpty(refundModel.getBatchNo(), "batchNo");
        refundParams.put(AlipayField.BATCH_NO.field(), refundModel.getBatchNo());

        // 退款明细
        List<RefundItemModel> detailDatas = refundModel.getDetailDatas();
        assertNotNullAndEmpty(detailDatas, "detail datas");
        refundParams.put(AlipayField.BATCH_NUM.field(), Integer.toString(detailDatas.size()));
        refundParams.put(AlipayField.DETAIL_DATA.field(), refundModel.formatDetailDatas());

        // md5签名参数
        buildMd5SignParams(refundParams);

        return refundParams;
    }
}
