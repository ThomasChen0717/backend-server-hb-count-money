package logic.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static final String encoding = "UTF-8";
    private static final int connectTimeout = 5000;
    private static final int socketTimeout = 30000;
    /**
     * 存储cookie
     */
    public static CookieStore cookieStore = new BasicCookieStore();

    /**
     * 将请求的数据转换为JSONObject
     *
     * @param request
     * @return
     */
    public static JSONObject getParameterMap(HttpServletRequest request) {
        var json = new JSONObject();
        var map = request.getParameterMap();
        if (map != null) {
            for (var i : map.entrySet()) {
                var values = i.getValue();
                if (values != null && values.length > 0) {
                    json.put(i.getKey(), values[0]);
                }
            }
            log.info("HttpUtil::getParameterMap:request params = {}", json);
        }

        return json;
    }

    /**
     * 获取字符串中指定键的值
     *
     * @param str
     * @param key
     * @return
     */
    public static String getParam(String str, String key) {
        var a = str.split("#")[0];        // 取"#"前的字符串

        var s1 = a.split("\\?");            // 转义
        if (s1.length > 1) {
            var s2 = s1[1];

            var P_COMM = String.format("(^|&)%s=([^&]*)(&|$)", key);
            var pattern = Pattern.compile(P_COMM);
            var matcher = pattern.matcher(s2);
            if (matcher.find())
                return matcher.group(2);
        }

        return null;
    }

    /**
     * 获取客户端ip
     *
     * @param request
     * @return
     */
    public static String clientIp(HttpServletRequest request) {
        String ipAddress;

        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet;
                    try {
                        inet = InetAddress.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;
    }

    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> params) {
        return get(url, params, null);
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        var client = HttpClients.createDefault();
        try {
            var builder = new URIBuilder(url);
            if (params != null) {
                var set = params.entrySet();
                for (Map.Entry<String, String> i : set) {
                    builder.setParameter(i.getKey(), i.getValue());
                }
            }

            var config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();
            var method = new HttpGet(builder.build());
            method.setConfig(config);

            packageHeader(headers, method);

            return fetchResult(client, method);
        } catch (Exception e) {
            log.error(e.getMessage());

            return null;
        } finally {
            release(client);
        }
    }

    // -------------- post: string ----------------------
    public static String post(String url, String data) {
        return post(url, data, null);
    }

    /**
     * post传递字符串参数
     *
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, String data, Map<String, String> headers) {
        var client = HttpClients.createDefault();
        try {
            var config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();
            var method = new HttpPost(url);
            method.setConfig(config);

            packageHeader(headers, method);
            packageParams(data, method);

            return fetchResult(client, method);
        } catch (Exception e) {
            log.error("Http.post(): Exception: {}, url={}, data={}", e.getMessage(), url, data);

            return null;
        } finally {
            release(client);
        }
    }

    // -------------- post: json ----------------------

    /**
     * post传递form数据
     *
     * @param url
     * @param json
     * @return
     */
    public static String post(String url, JSONObject json) {
        return post(url, json, "form");
    }

    public static String postJson(String url, JSONObject json) {
        return post(url, json, "json");
    }

    public static String postJson(String url, JSONObject json, boolean ignoreSsl) {
        return post(url, json, "json", ignoreSsl);
    }

    /**
     * post传递json数据，可指定contentType
     *
     * @param url
     * @param json
     * @param contentType: 默认为form, 然后就是json
     * @return
     */
    public static String post(String url, JSONObject json, String contentType) {
        return post(url, json, contentType, false);
    }

    /**
     * post传递json数据，可指定contentType
     *
     * @param url
     * @param json
     * @param contentType: 默认为form, 然后就是json
     * @return
     */
    public static String post(String url, JSONObject json, String contentType, boolean ignoreSsl) {
        var headers = new HashMap<String, String>();
        if ("form".equals(contentType))
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        else
            headers.put("Content-Type", "application/json;charset=utf-8");

        return post(url, json, contentType, headers, ignoreSsl);
    }

    public static String post(String url, JSONObject json, String contentType, Map<String, String> headers) {
        return post(url, json, contentType, headers, false);
    }

    /**
     * post提交json数据: 默认: Content-Type="application/x-www-form-urlencoded;charset=utf-8"
     *
     * @param url
     * @param json
     * @param headers
     * @return
     */
    public static String post(String url, JSONObject json, String contentType, Map<String, String> headers, boolean ignoreSsl) {
        CloseableHttpClient client = createHttpClient(ignoreSsl);

        try {
            int nSocketTimeout = socketTimeout;
            if (json.getInteger("socketTimeout") != null) {
                nSocketTimeout = json.getInteger("socketTimeout");
            }
            var config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(nSocketTimeout)
                    .build();
            var method = new HttpPost(url);
            method.setConfig(config);

            packageHeader(headers, method);
            packageParams(json, method, contentType);

            return fetchResult(client, method);
        } catch (Exception e) {
            log.error("Http.post(): Exception: {}, url={}, json={}", e.getMessage(), url, json);

            return null;
        } finally {
            release(client);
        }
    }

    /**
     * 获取待签名内容, 将json拼接成k1=v1&k2=v2的形式
     *
     * @param json
     * @param excludeKeys
     * @return
     */
    public static String getSignContent(JSONObject json, String[] excludeKeys) {
        var list = new ArrayList<String>();
        var copy = JSON.parseObject(JSON.toJSONString(json, SerializerFeature.MapSortField), Feature.OrderedField);
        if (excludeKeys != null) {
            for (var i : excludeKeys) {
                copy.remove(i);
            }
        }
        for (var i : copy.keySet()) {
            list.add(String.format("%s=%s", i, copy.get(i)));
        }

        return StringUtil.join(list, "&");
    }

    /**
     * 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    private static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        if (params != null) {
            var set = params.entrySet();
            for (Map.Entry<String, String> i : set) {
                httpMethod.setHeader(i.getKey(), i.getValue());
            }
        }
    }

    private static void packageParams(String data, HttpEntityEnclosingRequestBase httpMethod) {
        if (data != null) {
            try {
                httpMethod.setEntity(new StringEntity(data, encoding));
            } catch (Exception e) {
                log.error("packageParams(): {}", e.getMessage());
            }
        } else {
            log.error("packageParams(): data is null");
        }
    }

    /**
     * 封装请求参数
     *
     * @param json
     * @param httpMethod
     */
    private static void packageParams(JSONObject json, HttpEntityEnclosingRequestBase httpMethod, String contentType) {
        if (json != null) {
            try {
                if ("form".equals(contentType)) {
                    var list = new ArrayList<NameValuePair>();
                    for (var i : json.entrySet()) {
                        var value = i.getValue();
                        if (value != null) {
                            list.add(new BasicNameValuePair(i.getKey(), value.toString()));
                        }
                    }

                    httpMethod.setEntity(new UrlEncodedFormEntity(list, encoding));
                } else// if("json".equals(contentType))
                    httpMethod.setEntity(new StringEntity(json.toString(), encoding));
            } catch (Exception e) {
                log.error("packageParams(): {}", e.getMessage());
            }
        }
    }

    private static String fetchResult(CloseableHttpClient client, HttpRequestBase httpMethod) throws Exception {
        String ret = null;

        var response = client.execute(httpMethod);
        if (response != null && response.getStatusLine() != null) {
            var entity = response.getEntity();
            if (entity != null)
                ret = EntityUtils.toString(entity, encoding);

            response.close();
            log.info("http response released");
        }

        return ret;
    }

    private static void release(CloseableHttpClient client) {
        if (client != null) {
            try {
                client.close();
                log.info("http client released");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static CloseableHttpClient createHttpClient(boolean ignoreSsl) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultCookieStore(HttpUtil.cookieStore);

        if (ignoreSsl) {
            try {
                // 信任所有
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                        null, (TrustStrategy) (chain, authType) -> true).build();
                return httpClientBuilder.setSSLContext(sslContext).setSSLHostnameVerifier((x, y) -> true).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return httpClientBuilder.build();
    }
}
