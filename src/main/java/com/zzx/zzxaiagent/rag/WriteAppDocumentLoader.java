package com.zzx.zzxaiagent.rag;

import com.alibaba.cloud.ai.document.DocumentParser;
import com.alibaba.cloud.ai.document.TextDocumentParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取本地知识文件
 */
@Component
@Slf4j
public class WriteAppDocumentLoader {

    private final Resource[] resource;

    @Value("${github.token}")
    private String gitHubToken;

    WriteAppDocumentLoader(@Value("classpath:document/*.md") Resource[] resource) {
        this.resource = resource;
    }

    /**
     * 读取本地 markdown 文件
     *
     * @return
     */
    public List<Document> loadMarkdown() {
        List<Document> allDocuments = new ArrayList<>();
        for (Resource r : resource) {
            String filename = r.getFilename();
            MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                    .withHorizontalRuleCreateDocument(true)
                    .withIncludeCodeBlock(false)
                    .withIncludeBlockquote(false)
                    .withAdditionalMetadata("filename", filename)
                    .build();

            MarkdownDocumentReader reader = new MarkdownDocumentReader(r, config);
            allDocuments.addAll(reader.get());
        }
        return allDocuments;
    }



    /**
     * 读取 GitHub 单个文件
     *
     * @return 返回为切割分块后的Document列表
     */
    public List<Document> loadGitHub() {
        // 1) 创建 parser
        DocumentParser parser = new TextDocumentParser();   // 也可以用 MarkdownDocumentParser 等

        // 2) 构造 GitHubResource（链式 Builder）
        GitHubResource resource = GitHubResource.builder()
                .gitHubToken(gitHubToken)
                .owner("spring-projects")   // 仓库所有者
                .repo("spring-ai")          // 仓库名
                .branch("main")             // 分支（可选，默认为main）
                .path("README.md")          // 这里是要指定的文件
                .build();

        // 3) 读取为 Spring-AI Document
        GitHubDocumentReader reader = new GitHubDocumentReader(resource, parser);
        List<Document> docs = reader.get();

        // 4) 使用
        Document document = docs.getFirst();
        log.info("从GitHub读取的文件正文长度: {} ", document.getText().length());
        log.info("从GitHub读取的文件正文长度: 下载地址: {}", document.getMetadata().get("github_download_url"));

        // 5) 分块
        TokenTextSplitter splitter = new TokenTextSplitter(1500, 150, 5, 1000, true);
        List<Document> documents = splitter.split(document);
        return documents;
    }

}
