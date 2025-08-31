package com.zzx.zzxaiagent.baidu;

import java.util.HashMap;
import java.util.Map;

public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = this.buildParams(query, from, to);
        return HttpGet.get("http://api.fanyi.baidu.com/api/trans/vip/translate", params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", this.appid);
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        String src = this.appid + query + salt + this.securityKey;
        params.put("sign", MD5.md5(src));
        return params;
    }
}
