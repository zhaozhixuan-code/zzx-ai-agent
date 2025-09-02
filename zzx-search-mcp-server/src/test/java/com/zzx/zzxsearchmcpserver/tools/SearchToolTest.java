package com.zzx.zzxsearchmcpserver.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchToolTest {

    @Resource
    private SearchTool searchTool;

    @Test
    void search() {
        String result = searchTool.search("2022年8月的重要财经事件、政策变化和市场数据");
        System.out.println(result);

    }
}