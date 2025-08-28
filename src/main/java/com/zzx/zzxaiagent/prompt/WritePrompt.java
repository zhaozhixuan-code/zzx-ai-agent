package com.zzx.zzxaiagent.prompt;

public class WritePrompt {

    /**
     *  RE 2 拦截器提示词
     */
    public static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
            {re2_input_query}
            Read the question again: {re2_input_query}
            """;
}
