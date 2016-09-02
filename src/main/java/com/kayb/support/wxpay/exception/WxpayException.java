package com.kayb.support.wxpay.exception;

/**
 * Wxpay exception
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class WxpayException extends RuntimeException {

    /**
     * 当微信发生错误时，对应的错误码
     */
    private String errorCode;

    /**
     * 当微信发生错误时，对应的错误消息
     */
    private String errorMsg;

    public WxpayException(Throwable cause) {
        super(cause);
    }

    public WxpayException(String errorCode, String errorMsg){
        super("[" + errorCode + "]"+ errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
