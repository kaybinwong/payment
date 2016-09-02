package com.kayb.support.wxpay.model.bill;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Data
@ToString
public class BillDetail<T extends Bill> implements Serializable {

    private static final long serialVersionUID = 2763506940046963037L;

    /**
     * 账单记录
     */
    private List<T> bills;

    /**
     * 账单统计
     */
    private BillCount count;

    @SuppressWarnings("unchecked")
    private static final BillDetail EMPTY = (BillDetail)new BillDetail(Collections.emptyList(), null);

    public BillDetail(List<T> bills, BillCount count) {
        this.bills = bills;
        this.count = count;
    }

    @SuppressWarnings("unchecked")
    public static BillDetail empty(){
        return EMPTY;
    }
}
