package com.kayb.support.unionpay.core;

import java.util.Map;

/**
 * Verifies Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class Verifies extends Component {

    protected Verifies(UnionPay unionPay) {
        super(unionPay);
    }

    boolean verifies(Map<String, Object> parameter) {
        return doVerify(parameter);
    }

}
