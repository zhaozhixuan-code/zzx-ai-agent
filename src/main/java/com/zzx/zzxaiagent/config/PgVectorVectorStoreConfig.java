package com.zzx.zzxaiagent.config;

import com.zzx.zzxaiagent.rag.WriteAppDocumentLoader;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 *  基于 pgVector 的向量存储
 */
@Configuration
public class PgVectorVectorStoreConfig {
    @Resource
    private WriteAppDocumentLoader AppDocumentLoader;

    @Bean
    public VectorStore pgVectorVectorStore(
            @Qualifier("pgJdbcTemplate") JdbcTemplate pgJdbcTemplate,
            EmbeddingModel dashscopeEmbeddingModel) {
        PgVectorStore vectorStore = PgVectorStore.builder(pgJdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1536)                 // 可选：向量维度
                .distanceType(COSINE_DISTANCE)    // 可选：相似度算法
                .indexType(HNSW)                  // 可选：索引类型
                .initializeSchema(true)           // 首次启动自动建表、建 extension
                .schemaName("public")
                .vectorTableName("vector_store")
                .maxDocumentBatchSize(10000)
                .build();

        // 加载文档
        List<Document> documents = AppDocumentLoader.loadMarkdown();
        vectorStore.add(documents);
        return vectorStore;
    }
}