package com.zzx.zzxaiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取本地知识文件
 */
@Component
public class WriteAppDocumentLoader {

    private final Resource[] resource;

    WriteAppDocumentLoader(@Value("classpath:document/*.md") Resource[] resource) {
        this.resource = resource;
    }

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

}
