package com.kayb.support.alipay.model.refund;

import com.kayb.annotation.Optional;
import com.kayb.support.alipay.enums.AlipayField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Refund Model
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
public class RefundModel implements Serializable {

    private static final long serialVersionUID = -145560925778001071L;

    /**
     * 服务器异步通知页面路径
     * {@link AlipayField#NOTIFY_URL}
     */
    @Optional
    private String notifyUrl;


    /**
     * 退款批次号
     * {@link AlipayField#BATCH_NO}
     */
    private String batchNo;

    /**
     * 单笔数据集
     * {@link AlipayField#DETAIL_DATA}
     */
    private List<RefundItemModel> detailDatas;
    /**
     * 格式化退款数据
     * {@link AlipayField#DETAIL_DATA}
     * @return 退款数据
     */
    public String formatDetailDatas(){
        StringBuilder details = new StringBuilder();
        for (RefundItemModel data : detailDatas){
            details.append(data.format()).append("#");
        }
        details.deleteCharAt(details.length() - 1);
        return details.toString();
    }

    @Override
    public String toString() {
        return "RefundModel{" +
                "notifyUrl='" + notifyUrl + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", detailDatas=" + detailDatas +
                '}';
    }
}
