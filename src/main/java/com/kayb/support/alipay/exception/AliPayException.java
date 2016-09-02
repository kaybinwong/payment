package com.kayb.support.alipay.exception;

/**
 * ali exception
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class AliPayException extends RuntimeException {
    public AliPayException() {
        super();
    }

    public AliPayException(String message) {
        super(message);
    }

    public AliPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliPayException(Throwable cause) {
        super(cause);
    }

    protected AliPayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
