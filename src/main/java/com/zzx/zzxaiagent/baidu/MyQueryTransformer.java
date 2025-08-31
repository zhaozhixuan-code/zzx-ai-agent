package com.zzx.zzxaiagent.baidu;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 查询转换器
 * <p>
 * 基于百度翻译的查询翻译器
 */
@Component
public class MyQueryTransformer {

    @Value("${ai-agent.baidu.api-key}")
    private String APP_ID;
    @Value("${ai-agent.baidu.security-key}")
    private String SECURITY_KEY;


    /**
     * 翻译器
     *
     * @param message 要翻译的信息
     * @param to      要翻译成的语言
     * @return 翻译结果
     */
    public String doQueryTranslation(String message, String to) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String jsonStr = api.getTransResult(message, "auto", to);

        // 解析为JSONObject
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);

        // 获取trans_result数组
        JSONArray transResultArray = jsonObject.getJSONArray("trans_result");

        // 获取数组中的第一个元素，并提取dst字段
        String dst = transResultArray.getJSONObject(0).getStr("dst");

        return dst;
    }
}
