package com.kayb.util;

import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
import okio.ByteString;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.kayb.util.OkHttp.HttpMethod.GET;
import static com.kayb.util.OkHttp.HttpMethod.POST;

/**
 * Http Util base on okhttp
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Slf4j
public class OkHttp {

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public enum MediaType {

        PROTOBUF("application/x-protobuf"),
        JSON("application/json"),
        TEXT_PLAIN("text/plain"),
        TEXT_HTML("text/html"),
        TEXT_XML("text/xml"),
        POST_FROM("application/x-www-form-urlencoded");

        private String value;

        MediaType(String value) {
            this.value = value;
        }

        public com.squareup.okhttp.MediaType format() {
            return com.squareup.okhttp.MediaType.parse(value);
        }
    }

    private HttpMethod method = GET;
    private String url;

    private int connectTimeout = 1000 * 5;
    private int readTimeout = 1000 * 5;

    private Request.Builder request = new Request.Builder();
    private Headers.Builder headers = new Headers.Builder();
    private RequestBody body;

    private OkHttp(String url) {
        this.url = url;
    }

    private OkHttp method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public OkHttp header(String name, String value) {
        headers.add(name, value);
        return this;
    }

    public OkHttp headers(Map<String, String> headers) {
        Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            this.headers.add(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public OkHttp addParams(Map<String, String> params) {
        if (method.equals(POST)) {
            if(null != params && 0 < params.size()) {
                body = RequestBody.create(MediaType.POST_FROM.format(), getRequestParamString(params));
            }
            return this;
        }
        this.url = this.url.endsWith("?") ? url + concat(params) : url + "?" + concat(params);
        return this;
    }

    private String getRequestParamString(Map<String, String> requestParam) {
        StringBuffer sf = new StringBuffer("");
        String reqstr = "";
        if (null != requestParam && 0 != requestParam.size()) {
            for (Map.Entry<String, String> en : requestParam.entrySet()) {
                try {
                    sf.append(en.getKey()
                            + "="
                            + (null == en.getValue() || "".equals(en.getValue()) ? "" : URLEncoder
                            .encode(en.getValue(), "UTF-8")) + "&");
                } catch (UnsupportedEncodingException e) {
                    return "";
                }
            }
            reqstr = sf.substring(0, sf.length() - 1);
        }
        log.debug("encode url <=={}", reqstr);
        return reqstr;
    }

    private String concat(Map<String, String> params) {
        StringBuffer content = new StringBuffer();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            content.append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                content.append("&");
            }
        }
        return content.toString();
    }

    public OkHttp addParams(MediaType mediaType, Map<String, String> params) {
        if (method.equals(GET)) {
            return this;
        }
        return body(mediaType, concat(params));
    }

    /**
     * 请求body
     *
     * @param body request body
     * @return this
     */
    public OkHttp body(MediaType mediaType, String body) {
        this.body = RequestBody.create(mediaType.format(), body);
        return this;
    }

    public OkHttp body(MediaType mediaType, byte[] body) {
        this.body = RequestBody.create(mediaType.format(), body);
        return this;
    }

    public OkHttp body(MediaType mediaType, ByteString body) {
        this.body = RequestBody.create(mediaType.format(), body);
        return this;
    }

    /**
     * set connect timeout
     *
     * @param connectTimeout (s)
     * @return this
     */
    public OkHttp connTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout * 1000;
        return this;
    }

    /**
     * set read timeout
     *
     * @param readTimeout (s)
     * @return this
     */
    public OkHttp readTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout * 1000;
        return this;
    }

    private ResponseBody request() {

        OkHttpClient okHttpClient = getUnsafeOkHttpClient();
        okHttpClient.setReadTimeout(readTimeout, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        request.url(url);
        if (method.equals(GET)) {
            request.get();
        } else {
            request.post(body);
        }

        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            log.info("<http {} {}> response {} ", method, url, response);
            if (!response.isSuccessful() || response.code() != 200) {
                log.info("<http {} {}> response {} ", method, url, response);
            }

            return response.body();
        } catch (IOException e) {
            log.error("error when {} {} with body {}", method, url, body, e);
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        ResponseBody responseBody = request();
        if (responseBody == null) return null;
        try {
            return responseBody.string();
        } catch (IOException e) {
            log.error("error when parse to string with body {}", responseBody.toString(), e);
            return "";
        }
    }

    public byte[] toByte() {
        ResponseBody responseBody = request();
        if (responseBody == null) return null;
        try {
            return responseBody.bytes();
        } catch (IOException e) {
            log.error("error when parse to string with body {}", responseBody.toString(), e);
            return new byte[0];
        }
    }

    public InputStream toInputStream() {
        ResponseBody responseBody = request();
        if (responseBody == null) return null;
        try {
            return responseBody.byteStream();
        } catch (IOException e) {
            log.error("error when parse to string with body {}", responseBody.toString(), e);
            return null;
        }
    }

    public <T> T toBean(Class<T> clazz) {

        ResponseBody responseBody = request();

        if (responseBody == null) return null;
        try {
            return JsonUtil.toBean(responseBody.string(), clazz);
        } catch (IOException e) {
            log.error("error when parse to {} with body {}", clazz.getSimpleName(), responseBody.toString(), e);
            return null;
        }
    }

    public static OkHttp get(String url) {
        return new OkHttp(url);
    }

    public static OkHttp post(String url) {
        return new OkHttp(url).method(POST);
    }

    public static OkHttp put(String url) {
        return new OkHttp(url).method(HttpMethod.PUT);
    }

    public static OkHttp delete(String url) {
        return new OkHttp(url).method(HttpMethod.DELETE);
    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(OkHttp.get("http://ip.taobao.com/service/getIpInfo.php?ip=210.75.225.254").toString());
    }

}
