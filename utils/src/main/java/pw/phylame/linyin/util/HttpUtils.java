/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import lombok.Data;
import pw.phylame.linyin.constants.Constants;

/**
 * @author Peng Wan
 */
public class HttpUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(HttpSession session, String name, T defaultValue) {
        Object o = session.getAttribute(name);
        return o != null ? (T) o : defaultValue;
    }

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String makeQuery(String... strings) {
        if (Objects.requireNonNull(strings).length % 2 != 0) {
            throw new IllegalArgumentException("length of strings must %2 = 0");
        }
        StringBuilder sb = new StringBuilder("?");
        int end = strings.length, last = strings.length - 2;
        try {
            for (int i = 0; i < end; i += 2) {
                sb.append(strings[i]).append('=').append(URLEncoder.encode(strings[i + 1], Constants.ENCODING));
                if (i != last) {
                    sb.append('&');
                }
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return sb.toString();
    }

    public static String makeRequest(String url, Map<String, String> params) {
        if (MiscUtils.isEmpty(params)) {
            return url;
        }
        StringBuilder result = new StringBuilder(url);
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (i++ == 0) {
                result.append('?');
            } else {
                result.append('&');
            }
            result.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return result.toString();
    }

    public static void setRequestProperties(URLConnection conn, Map<String, String> props) {
        if (!MiscUtils.isEmpty(props)) {
            for (Map.Entry<String, String> entry : props.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void writeRequestData(URLConnection conn, byte[] data) throws IOException {
        try (OutputStream out = conn.getOutputStream()) {
            out.write(data);
            out.flush();
        }
    }

    public static void writeRequestData(URLConnection conn, Map<String, String> params, String encoding)
            throws IOException {
        if (MiscUtils.isEmpty(params)) {
            return;
        }
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            data.append('&').append(entry.getKey()).append('=').append(entry.getValue());
        }
        writeRequestData(conn, encoding != null ? data.toString().getBytes(encoding) : data.toString().getBytes());
    }

    /**
     * 发送GET请求
     *
     * @param url URL地址
     * @return 响应对象
     * @throws IOException
     */
    public static Response sendGet(String url) throws IOException {
        return send(url, "GET", null, null);
    }

    /**
     * 发送GET请求
     *
     * @param url    URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException
     */
    public static Response sendGet(String url, Map<String, String> params) throws IOException {
        return send(url, "GET", params, null);
    }

    /**
     * 发送GET请求
     *
     * @param url    URL地址
     * @param params 参数集合
     * @param props  请求属性
     * @return 响应对象
     * @throws IOException
     */
    public static Response sendGet(String url, Map<String, String> params, Map<String, String> props)
            throws IOException {
        return send(url, "GET", params, props);
    }

    /**
     * 发送POST请求
     *
     * @param url URL地址
     * @return 响应对象
     * @throws IOException
     */
    public static Response sendPost(String url) throws IOException {
        return send(url, "POST", null, null);
    }

    /**
     * 发送POST请求
     *
     * @param url    URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException
     */
    public static Response sendPost(String url, Map<String, String> params) throws IOException {
        return send(url, "POST", params, null);
    }

    /**
     * 发送POST请求
     *
     * @param url    URL地址
     * @param params 参数集合
     * @param props  请求属性
     * @return 响应对象
     * @throws IOException
     */
    public static Response sendPost(String url, Map<String, String> params, Map<String, String> props)
            throws IOException {
        return send(url, "POST", params, props);
    }

    public static Response sendPost(String url, byte[] data, Map<String, String> props) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        setRequestProperties(conn, props);
        writeRequestData(conn, data);
        conn.connect();
        return makeContent(url, conn);
    }

    /**
     * 发送HTTP请求
     *
     * @param url
     * @return 响映对象
     * @throws IOException
     */
    private static Response send(String url, String method, Map<String, String> params, Map<String, String> props)
            throws IOException {
        if ("GET".equalsIgnoreCase(method)) {
            url = makeRequest(url, params);
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        setRequestProperties(conn, props);
        if ("POST".equalsIgnoreCase(method)) {
            writeRequestData(conn, params, null);
        }
        conn.connect();
        return makeContent(url, conn);
    }

    /**
     * 得到响应对象
     *
     * @param conn
     * @return 响应对象
     * @throws IOException
     */
    private static Response makeContent(String url, HttpURLConnection conn) throws IOException {
        Response response = new Response();
        try {
            response.url = url;
            response.defaultPort = conn.getURL().getDefaultPort();
            response.file = conn.getURL().getFile();
            response.host = conn.getURL().getHost();
            response.path = conn.getURL().getPath();
            response.port = conn.getURL().getPort();
            response.protocol = conn.getURL().getProtocol();
            response.query = conn.getURL().getQuery();
            response.ref = conn.getURL().getRef();
            response.userInfo = conn.getURL().getUserInfo();
            response.method = conn.getRequestMethod();
            response.connectTimeout = conn.getConnectTimeout();
            response.readTimeout = conn.getReadTimeout();
            response.code = conn.getResponseCode();
            response.message = conn.getResponseMessage();
            response.contentType = conn.getContentType();
            response.contentEncoding = conn.getContentEncoding();
            response.headers = conn.getHeaderFields();
            response.content = IOUtils.toByteArray(conn);
            return response;
        } finally {
            conn.disconnect();
        }
    }

    @Data
    public static class Response {
        private String url;
        private int defaultPort;
        private String host;
        private int port;
        private String path;
        private String file;
        private String protocol;
        private String contentEncoding;
        private String contentType;
        private byte[] content;
        private String message;
        private String method;
        private String query;
        private String ref;
        private String userInfo;
        private int code;
        private int connectTimeout;
        private int readTimeout;
        private Map<String, List<String>> headers;
    }
}
