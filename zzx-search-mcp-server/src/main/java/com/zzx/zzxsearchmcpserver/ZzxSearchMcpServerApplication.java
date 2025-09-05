package com.zzx.zzxsearchmcpserver;

import com.zzx.zzxsearchmcpserver.tools.SearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ZzxSearchMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzxSearchMcpServerApplication.class, args);
    }
/*     @Bean
    public ToolCallbackProvider imageSearchTools(SearchTool tool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(tool)
                .build();
    } */
}
