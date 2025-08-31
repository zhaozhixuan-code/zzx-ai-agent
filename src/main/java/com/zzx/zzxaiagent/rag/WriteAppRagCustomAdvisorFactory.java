package com.zzx.zzxaiagent.rag;

import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 创建自定义的rag检索增强顾问的工厂
 */
public class WriteAppRagCustomAdvisorFactory {

    public static Advisor doCreate(VectorStore vectorStore, String status){
        // 过滤特定状态的文档
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();

        //  创建文档检索器
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.5)
                .topK(5)
                .filterExpression(expression)
                .build();

        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .queryAugmenter(WriteAppContextualQueryAugmenterFactory.createInstance())
                .build();
        return retrievalAugmentationAdvisor;
    }
}