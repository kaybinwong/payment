package com.kayb.support.unionpay.core;

import com.kayb.support.unionpay.enums.UnionPayField;
import com.kayb.util.SecureUtil;
import com.kayb.util.OkHttp;
import com.kayb.util.Preconditions;
import com.kayb.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.kayb.util.Preconditions.*;

/**
 * Payment Component
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Slf4j
public abstract class Component {

    protected UnionPay unionPay;

    protected Component(UnionPay unionPay){
        this.unionPay = unionPay;
    }

    /**
     * 构建配置参数
     * @param params 参数
     */
    protected void buildConfigParams(final Map<String, String> params){
//        params.put(UnionPayField.MER_ID, unionPay.merId);
        params.put(UnionPayField.CERT_ID, unionPay.certId);
    }

    /**
     * 构建签名参数
     * @param params 支付参数
     */
    protected void buildSignParams(final Map<String, String> params) {
        String sign = doSign(params);
        log.debug("sign <=={}", sign);
        params.put(UnionPayField.SIGNATURE, sign);
    }

    /**
     * 数据提交 提交到后台
     * @param params
     * @param requestUrl
     * @return
     */
    Map<String, String> doPost(Map<String, String> params, String requestUrl) {

        assertNotNullAndEmpty(requestUrl, "requestUrl");
        buildSignParams(params);
        log.debug("requestUrl=>{}", requestUrl);
        log.debug("submitFromData=>{}", params.toString());
        String resultString = OkHttp
                .post(requestUrl)
                .addParams(params)
                .toString();
        Map<String, String> resData =null;
        log.debug("post rsp <=={}", resultString);
        if (!isEmpty(resultString)) {
            // 将返回结果转换为map
            Map<String, String> data = convertResultStringToMap(resultString);
            if (doVerify(data)) {
                resData=data;
                log.debug("验证签名成功");
            } else {
                log.debug("验证签名失败");
            }
        }
        return resData;
    }

    /**
     * 校验参数
     * @param data 待校验参数
     * @return 校验成功返回true，反之false
     */
    Boolean doVerify(final Map<String, ?> data) {

        Preconditions.assertNotNullAndEmpty(unionPay.appPubKey, "app public key can't be empty before rsa verify.");

        String actualSign = String.valueOf(data.get(UnionPayField.SIGNATURE));
        log.debug("返回的签名:[{}]", actualSign);
        String signing = buildSignString(data);
        byte[] signDigest = SecureUtil.sha1X16(signing, unionPay.encoding);
        return RSAUtil.verify(signDigest, actualSign, unionPay.appPubKey);
    }

    /**
     * 生成签名值(SHA1摘要算法)
     * @param data 待签名数据Map键值对形式编码
     * @return 签名结果
     */
    String doSign(final Map<String, String> data) {
        buildConfigParams(data);
        String stringData = buildSignString(data);
        String stringSign = null;
        try {
            // 通过SHA1进行摘要并转16进制
            byte[] signDigest = SecureUtil.sha1X16(stringData, unionPay.encoding);
            stringSign = RSAUtil.sign(signDigest, unionPay.appPriKey);
        } catch (Exception e) {
            log.error("签名异常", e);
            e.printStackTrace();
        }
        return stringSign;
    }

    /**
     * 过滤签名参数(升序，排除空值，signature)
     * @param params 待校验参数
     * @return 过滤后的参数
     */
    Map<String, String> filterSigningParams(final Map<String, ?> params) {
        Map<String, String> validParams = new TreeMap<>();
        for (Map.Entry<String, ?> param : params.entrySet()){
            if (UnionPayField.SIGNATURE.equals(param.getKey())
                    || param.getValue() == null
                    || "".equals(String.valueOf(param.getValue()))){
                continue;
            }
            validParams.put(param.getKey(), String.valueOf(param.getValue()).trim());
        }
        return validParams;
    }

    /**
     * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式 不包含签名域signature
     * @param params 待拼接的Map数据
     * @return 拼接好后的字符串
     */
    String buildSignString(Map<String, ?> params) {

        Map<String, String> validParams = filterSigningParams(params);
        List<String> keys = new ArrayList<>(validParams.keySet());
        Collections.sort(keys);

        StringBuilder payString = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = validParams.get(key);
            payString.append(key).append("=").append(value).append("&");
        }
        payString.deleteCharAt(payString.length() - 1);
        String signString = payString.toString();
        log.debug("待签名报文串->>{}", signString);
        return signString.toString();
    }

    /**
     * 将形如key=value&key=value的字符串转换为相应的Map对象
     * @param result
     * @return
     */
    Map<String, String> coverResultString2Map(String result) {
        return convertResultStringToMap(result);
    }

    /**
     * 将形如key=value&key=value的字符串转换为相应的Map对象
     * @param result
     * @return
     */
    Map<String, String> convertResultStringToMap(String result) {
        Map<String, String> map;
        if(StringUtils.isNotBlank(result)){
            if(result.startsWith("{") && result.endsWith("}")){
                result = result.substring(1, result.length()-1);
            }
            map = parseQString(result);
        }else {
            map = new HashMap<>();
        }
        return map;
    }
    /**
     * 解析应答字符串，生成应答要素
     * @param str 需要解析的字符串
     * @return 解析的结果map
     */
    Map<String, String> parseQString(String str){
        Map<String, String> map = new HashMap<>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if(len>0){
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key
                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else  {// 如果当前生成的是value
                    if(isOpen){
                        if(curChar == openName){
                            isOpen = false;
                        }

                    }else{//如果没开启嵌套
                        if(curChar == '{'){//如果碰到，就开启嵌套
                            isOpen = true;
                            openName ='}';
                        }
                        if(curChar == '['){
                            isOpen = true;
                            openName =']';
                        }
                    }
                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    }else{
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, temp.toString());
        }
    }

    /**
     * 判断字符串是否为NULL或空
     * @param s 待判断的字符串数据
     * @return 判断结果 true-是 false-否
     */
    boolean isEmpty(String s) {
        return null == s || "".equals(s.trim());
    }

}
