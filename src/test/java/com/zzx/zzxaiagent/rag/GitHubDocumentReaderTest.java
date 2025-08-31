package com.zzx.zzxaiagent.rag;

import com.alibaba.cloud.ai.document.DocumentParser;
import com.alibaba.cloud.ai.document.TextDocumentParser;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GitHubDocumentReaderTest {

    @Test
    void get() {
        // 1) 创建 parser
        DocumentParser parser = new TextDocumentParser();   // 也可以用 MarkdownDocumentParser 等

        // 2) 构造 GitHubResource（链式 Builder）
        GitHubResource resource = GitHubResource.builder()
                .gitHubToken("")
                .owner("spring-projects")
                .repo("spring-ai")
                .branch("main")
                .path("README.md")
                .build();

        // 3) 读取为 Spring-AI Document
        GitHubDocumentReader reader = new GitHubDocumentReader(resource, parser);
        List<Document> docs = reader.get();

        // 4) 使用
        Document doc = docs.get(0);
        System.out.println("正文长度: " + doc.getText().length());
        System.out.println("下载地址: " + doc.getMetadata().get("github_download_url"));

    }
}