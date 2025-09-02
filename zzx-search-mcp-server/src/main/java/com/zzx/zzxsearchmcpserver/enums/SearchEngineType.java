package com.zzx.zzxsearchmcpserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchEngineType {

    /**
     * 基础版（智谱AI 自研）：满足日常查询需求，性价比极高
     */
    SEARCH_STD("search_std", "基础版"),

    /**
     * 高级版（智谱AI 自研）：多引擎协作显著降低空结果率，召回率和准确率大幅提升
     */
    SEARCH_PRO("search_pro", "高级版"),

    /**
     * 搜狗：覆盖腾讯生态（新闻/企鹅号）和知乎内容，在百科、医疗等垂直领域权威性强
     */
    SEARCH_PRO_SOGOU("search_pro_sogou", "搜狗"),

    /**
     * 夸克：精准触达垂直内容
     */
    SEARCH_PRO_QUARK("search_pro_quark", "夸克");

    /** 传递给接口的字符串编码 */
    private final String value;

    /** 中文描述，方便日志 / 文档展示 */
    private final String desc;
}
