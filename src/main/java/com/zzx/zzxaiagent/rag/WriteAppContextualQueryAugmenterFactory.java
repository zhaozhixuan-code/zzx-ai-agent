package com.zzx.zzxaiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建上下文查询增强器的工厂
 */
public class WriteAppContextualQueryAugmenterFactory {
    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                        你应该输出下面的内容：
                        抱歉，我只能回答写作相关的问题，别的没办法回答您
                        """
        );

        ContextualQueryAugmenter augmenter = ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
        return augmenter;
    }

}