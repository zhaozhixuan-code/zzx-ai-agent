package com.zzx.zzxaiagent.baidu;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

class HttpGet {
    protected static final int SOCKET_TIMEOUT = 10000;
    protected static final String GET = "GET";
    private static TrustManager myX509TrustManager = new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    };

    HttpGet() {
    }

    public static String get(String host, Map<String, String> params) {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init((KeyManager[]) null, new TrustManager[]{myX509TrustManager}, (SecureRandom) null);
            String sendUrl = getUrlWithQueryString(host, params);
            URL uri = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(sslcontext.getSocketFactory());
            }

            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                System.out.println("Http错误码：" + statusCode);
            }

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            String text = builder.toString();
            close(br);
            close(is);
            conn.disconnect();
            return text;
        } catch (MalformedURLException var12) {
            MalformedURLException e = var12;
            e.printStackTrace();
        } catch (IOException var13) {
            IOException e = var13;
            e.printStackTrace();
        } catch (KeyManagementException var14) {
            KeyManagementException e = var14;
            e.printStackTrace();
        } catch (NoSuchAlgorithmException var15) {
            NoSuchAlgorithmException e = var15;
            e.printStackTrace();
        }

        return null;
    }

    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        } else {
            StringBuilder builder = new StringBuilder(url);
            if (url.contains("?")) {
                builder.append("&");
            } else {
                builder.append("?");
            }

            int i = 0;
            Iterator var5 = params.keySet().iterator();

            while (var5.hasNext()) {
                String key = (String) var5.next();
                String value = (String) params.get(key);
                if (value != null) {
                    if (i != 0) {
                        builder.append('&');
                    }

                    builder.append(key);
                    builder.append('=');
                    builder.append(encode(value));
                    ++i;
                }
            }

            return builder.toString();
        }
    }

    protected static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
                IOException e = var2;
                e.printStackTrace();
            }
        }

    }

    public static String encode(String input) {
        if (input == null) {
            return "";
        } else {
            try {
                return URLEncoder.encode(input, "utf-8");
            } catch (UnsupportedEncodingException var2) {
                UnsupportedEncodingException e = var2;
                e.printStackTrace();
                return input;
            }
        }
    }
}
