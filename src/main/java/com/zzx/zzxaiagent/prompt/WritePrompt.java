package com.zzx.zzxaiagent.prompt;

public class WritePrompt {

    /**
     * RE 2 拦截器提示词
     */
    public static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
            {re2_input_query}
            Read the question again: {re2_input_query}
            """;


    public static final String cosplay = """
            Generate a highly detailed photo of a girl cosplaying this illustration, at Comiket. Exactly replicate the same pose, body posture, hand gestures, facial expression, and camera framing as in the original illustration. Keep the same angle, perspective, and composition, without any deviation
            """;
}
