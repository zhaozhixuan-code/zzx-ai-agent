package com.zzx.zzxaiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 联网搜索工具
 */

public class WebSearchTool {

    private final String API_URL = "https://www.searchapi.io/api/v1/search";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }


    /**
     * 执行百度搜索
     *
     * @param query 搜索关键词
     * @return String 格式的搜索结果
     */
    @Tool(description = "searchWeb for information from baidu searchWeb engine")
    public String searchWeb(@ToolParam(description = "searchWeb query keyword") String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("engine", "baidu");
        params.put("q", query);
        params.put("api_key", this.apiKey);

        try {
            String response = HttpUtil.get(API_URL, params);
            // 取出返回结果的前 5 条
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 提取 organic_results 部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            List<Object> objects = organicResults.subList(0, 5);
            // 拼接搜索结果为字符串
            String result = objects.stream().map(obj -> {
                JSONObject tmpJSONObject = (JSONObject) obj;
                return tmpJSONObject.toString();
            }).collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "searching baidu error" + e.getMessage();
        }
    }
}
