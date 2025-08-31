package com.zzx.zzxaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 基于内存的向量存储
 */
@Configuration
public class WriteAppVectorStoreConfig {

    @Resource
    private WriteAppDocumentLoader writeAppDocumentLoader;

    @Bean
    VectorStore WriteAppVectorStore(EmbeddingModel dashScopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        List<Document> documents = writeAppDocumentLoader.loadMarkdown();
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }

}


