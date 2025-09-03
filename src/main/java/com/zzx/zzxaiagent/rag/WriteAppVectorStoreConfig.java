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


    /**
     * 存储 markdown 文件
     *
     * @param dashScopeEmbeddingModel
     * @return
     */
    @Bean
    VectorStore writeAppVectorStore(EmbeddingModel dashScopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        // 获取本地 markdown 文件
        List<Document> documents = writeAppDocumentLoader.loadMarkdown();
        // 获取从 GitHub 上的文件
        // List<Document> documents1 = writeAppDocumentLoader.loadGitHub();
        // 添加文件到向量存储
        simpleVectorStore.add(documents);
        // simpleVectorStore.add(documents1);
        return simpleVectorStore;
    }

}


