package com.kayb.support.wxpay.core;

import com.kayb.support.wxpay.exception.WxpayException;
import com.kayb.support.wxpay.model.enums.WxpayField;
import com.kayb.util.Maps;
import com.kayb.util.JsonUtil;
import com.kayb.util.MD5Util;
import com.kayb.util.OkHttp;
import com.kayb.util.StringUtil;
import com.kayb.util.xml.XmlReaders;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * Base Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Slf4j
public abstract class Component {

    protected OkHttp.MediaType xml = OkHttp.MediaType.TEXT_XML;

    protected Wxpay wxpay;

    protected Component(Wxpay wxpay){
        this.wxpay = wxpay;
    }

    protected Map<String, Object> doPost(final String url, final Map<String, String> params){
        String requestBody = Maps.toXml(params);

        log.debug("doPost url ==> {}", url);
        log.debug("doPost requestBody ==> {}", requestBody);
        String resp = OkHttp.post(url).body(xml, requestBody).toString();
        Map<String, Object> respMap = toMap(resp.replaceAll("(\\r|\\n)", ""));
        doVerifySign(respMap);
        return respMap;
    }

    protected <T> T doHttpsPost(final String url, final Map<String, String> params, Class<T> respClazz){
        String requestBody = Maps.toXml(params);
        String resp = OkHttp.post(url).body(xml, requestBody).toString();
        Map<String, Object> respMap = toMap(resp.replaceAll("(\\r|\\n)", ""));
        doVerifySign(respMap);
        return JsonUtil.toBean(JsonUtil.toJson(respMap), respClazz);
    }


    /**
     * 将微信XML转换为Map
     * @param xml xml字符串
     * @return Map对象
     */
    protected Map<String, Object> toMap(final String xml) {
        XmlReaders readers = readResp(xml);
        return Maps.toMap(readers);
    }

    /**
     * 读取微信xml响应
     * @param xml xml字符串
     * @return 若成功，返回对应Reader，反之抛WepayException
     */
    private XmlReaders readResp(final String xml) {
        XmlReaders readers = XmlReaders.create(xml);
        String returnCode = readers.getNodeStr(WxpayField.RETURN_CODE);
        if (WxpayField.SUCCESS.equals(returnCode)){
            String resultCode = readers.getNodeStr(WxpayField.RESULT_CODE);
            if (WxpayField.SUCCESS.equals(resultCode)){
                return readers;
            }
            throw new WxpayException(
                    readers.getNodeStr(WxpayField.ERR_CODE),
                    readers.getNodeStr(WxpayField.ERR_CODE_DES));
        }
        throw new WxpayException(
                readers.getNodeStr(WxpayField.RETURN_CODE),
                readers.getNodeStr(WxpayField.RETURN_MSG));
    }

    /**
     * 构建配置参数
     * @param params 参数
     */
    protected void buildConfigParams(final Map<String, String> params){
        params.put(WxpayField.APP_ID, wxpay.getAppId());
        params.put(WxpayField.MCH_ID, wxpay.getMchId());
    }

    /**
     * 构建签名参数
     * @param params 支付参数
     */
    protected void buildSignParams(final Map<String, String> params) {
        String sign = doSign(params);
        put(params, WxpayField.SIGN, sign);
    }

    /**
     * 支付请求前签名
     * @param params 参数(已经升序, 排出非空值和sign)
     * @return MD5的签名字符串(大写)
     */
    protected String doSign(final Map<String, String> params) {
        StringBuilder signing = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!StringUtil.isEmpty(entry.getValue())){
                signing.append(entry.getKey()).append('=').append(entry.getValue()).append("&");
            }
        }

        // append key
        signing.append("key=").append(wxpay.getAppKey());

        // md5
        return MD5Util.generate(signing.toString()).toUpperCase();
    }

    /**
     * 校验
     * @param xml 微信xml内容
     * @return 校验成功返回true，反之false
     */
    protected Boolean doVerifySign(final String xml) {
        return doVerifySign(toMap(xml));
    }

    /**
     * 校验参数
     * @param data 待校验参数
     * @return 校验成功返回true，反之false
     */
    protected Boolean doVerifySign(final Map<String, ?> data) {
        String actualSign = String.valueOf(data.get(WxpayField.SIGN));
        Map<String, String> signingMap = filterSignParams(data);
        String expectSign = doSign(signingMap);
        return expectSign.equals(actualSign);
    }

    /**
     * 过滤签名参数(升序，排出空值，sign)
     * @param params 待校验参数
     * @return 过滤后的参数
     */
    protected Map<String, String> filterSignParams(final Map<String, ?> params) {
        Map<String, String> validParams = new TreeMap<>();

        for (Map.Entry<String, ?> param : params.entrySet()){
            if (WxpayField.SIGN.equals(param.getKey())
                    || param.getValue() == null
                    || "".equals(String.valueOf(param.getValue()))){
                continue;
            }
            validParams.put(param.getKey(), String.valueOf(param.getValue()));
        }

        return validParams;
    }

    protected void putIfNotEmpty(final Map<String, String> map, String field, String paramValue) {
        if (!StringUtil.isEmpty(paramValue)){
            map.put(field, paramValue);
        }
    }

    protected void put(final Map<String, String> map, String field, String paramValue){
        map.put(field, paramValue);
    }
}
