package com.gongsibao.common.util.http;

import com.gongsibao.common.util.InputStreamUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * httpclient 工具类
 *
 * @author wwj <wangweijie@techwolf.cn>
 * @ClassName: HttpClientUtil
 * @date 2014年1月24日 下午12:23:58
 */
public class HttpClientUtil {

    private static Log log = LogFactory.getLog(HttpClientUtil.class);

    private static Registry<ConnectionSocketFactory> socketFactoryRegistry = null;

    static {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

    static {
        //设置连接池线程最大数量
        cm.setMaxTotal(50);
        //设置单个路由最大的连接线程数量
        cm.setDefaultMaxPerRoute(10);
    }

    private static final int SOCKET_TIMEOUT = 60 * 1000; //超时时间1分钟
    private static final int CONNECT_TIMEOUT = 60 * 1000; //连接超时时间1分钟
    private static CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm)
            .setDefaultCookieStore(new BasicCookieStore()).build();
    private static RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
            .setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();

    private static HttpGet getHttpGet(String url) {
        HttpGet get = new HttpGet(url);
        Builder builder = RequestConfig.copy(globalConfig);
        get.setConfig(builder.build());
        return get;
    }

    private static HttpPost getHttpPost(String url) {
        HttpPost post = new HttpPost(url);
        Builder builder = RequestConfig.copy(globalConfig);
        post.setConfig(builder.build());
        return post;
    }

    private static HttpPut getHttpPut(String url) {
        HttpPut put = new HttpPut(url);
        Builder builder = RequestConfig.copy(globalConfig);
        put.setConfig(builder.build());
        return put;
    }

    private static HttpDelete getHttpDelete(String url) {
        HttpDelete delete = new HttpDelete(url);
        Builder builder = RequestConfig.copy(globalConfig);
        delete.setConfig(builder.build());
        return delete;
    }

    /**
     * 发送HTTPGET请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String httpGet(String url, String charset) throws IOException {
        String html;
        HttpGet get = getHttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httpget url=" + url);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != get) get.releaseConnection();
        }
        return StringUtils.trimToEmpty(html);
    }

    /**
     * 发送HTTPGET请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String httpGetNoException(String url, String charset) {
        String html = null;
        HttpGet get = getHttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httpget url=" + url);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            if (null != get) get.releaseConnection();
        }
        return StringUtils.trimToEmpty(html);
    }

    /**
     * 发送HTTPGET请求
     *
     * @param url
     * @param charset
     * @param headerMap header参数
     * @return
     * @throws IOException
     */
    public static String httpGet(String url, String charset, Map<String, String> headerMap) throws IOException {
        String html;
        HttpGet get = getHttpGet(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Entry<String, String> tmpEntry : headerMap.entrySet()) {
                get.setHeader(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httpget url=" + url);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != get) get.releaseConnection();
        }
        return StringUtils.trimToEmpty(html);
    }

    /**
     * 发送HTTP get请求
     *
     * @param url
     * @param params
     * @param charset
     * @return
     * @throws IOException
     */
    public static String httpGet(String url, Map<String, String> params, String charset)
            throws IOException {
        String html = null;
        StringBuffer data = new StringBuffer();
        if (null != params) {
            for (Entry<String, String> entry : params.entrySet()) {
                data.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        if (data.length() > 1) {
            data = data.deleteCharAt(data.length() - 1);
        }

        if (url.endsWith("?")) {
            url = url + data.toString();
        } else if (StringUtils.isBlank(data)) {
            //没有参数就不加问号了,否则微信认证失败
        } else {
            url = url + "?" + data.toString();
        }
        log.debug("httpget requesturl=" + url);
        HttpGet get = getHttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httpget response=" + html);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != get) get.releaseConnection();
        }
        return html;
    }

    /**
     * 发送post 请求
     *
     * @param url
     * @param params
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String httpPost(String url, Map<String, String> params, String charset)
            throws IOException {
        String html = null;
        HttpPost post = getHttpPost(url);
        if (null != params) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, charset));
        }
        try {
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httppost respose=" + html);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != post) {
                post.releaseConnection();
            }
        }
        return html;
    }


    /**
     * 发送post 请求
     *
     * @param url
     * @param body
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String httpPost(String url, String body, String charset) throws IOException {
        String html = null;
        HttpPost post = getHttpPost(url);
        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, charset));
        }
        try {
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httppost respose=" + html);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != post) {
                post.releaseConnection();
            }
        }
        return html;
    }

    public static String httpPost(String url, Map<String, String> headers, String body, String charset) throws IOException {
        String html = null;
        HttpPost post = getHttpPost(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, charset));
        }
        try {
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httppost respose=" + html);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != post) {
                post.releaseConnection();
            }
        }
        return html;
    }

    /**
     * 获取URL请求状态码
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static int getUrlStatusCode(String url) {
        int statusCode = 0;
        HttpGet get = null;
        try {
            get = getHttpGet(url);
            CloseableHttpResponse response = client.execute(get);
            statusCode = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != get) {
                get.releaseConnection();
            }
        }
        return statusCode;
    }

    /**
     * 获取请求的信息
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpResponseModel httpGet(String url) throws IOException {
        HttpResponseModel httpResponseModel = new HttpResponseModel();
        HttpEntity httpEntity = null;
        HttpGet get = getHttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();
            byte[] filebytes = InputStreamUtil.transferInputStream2Bytes(is);
            if (filebytes != null && filebytes.length > 0) {
                httpResponseModel.setBytes(filebytes);
            }
            Header header = httpEntity.getContentType();
            if (StringUtils.isNotBlank(header.toString())) {
                httpResponseModel.setContentType(header.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != get) {
                get.releaseConnection();
            }
        }
        return httpResponseModel;
    }

    public static String httpPut(String url, Map<String, String> headers, String body, String charset) throws IOException {
        String html = null;
        HttpPut put = getHttpPut(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Entry<String, String> entry : headers.entrySet()) {
                put.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (StringUtils.isNotBlank(body)) {
            put.setEntity(new StringEntity(body, charset));
        }
        try {
            CloseableHttpResponse response = client.execute(put);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httpput respose=" + html);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != put) {
                put.releaseConnection();
            }
        }
        return html;
    }

    public static String httpDelete(String url, String charset, Map<String, String> headers) throws IOException {
        String html;
        HttpDelete delete = getHttpDelete(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Entry<String, String> tmpEntry : headers.entrySet()) {
                delete.setHeader(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        try {
            CloseableHttpResponse response = client.execute(delete);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, charset);
            log.debug("httpget url=" + url);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != delete) delete.releaseConnection();
        }
        return StringUtils.trimToEmpty(html);
    }

    public static String httpRequest(String url, Map<String, String> headers, String body, String charset, String method) throws IOException {
        if (method.equals(HttpPost.METHOD_NAME)) {
            return httpPost(url, headers, body, charset);
        } else if (method.equals(HttpPut.METHOD_NAME)) {
            return httpPut(url, headers, body, charset);
        } else if (method.equals(HttpGet.METHOD_NAME)) {
            return httpGet(url, charset, headers);
        } else if (method.equals(HttpDelete.METHOD_NAME)) {
            return httpDelete(url, charset, headers);
        } else {
            return httpGet(url, charset, headers);
        }
    }

//    public static void main(String[] args) {
//        try {
//            System.out.println(httpGet("http://www.gongsibao.com/", "utf-8"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
