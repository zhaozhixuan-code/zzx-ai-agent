package com.zzx.zzxaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ZzxManusTest {

    @Resource
    private ZzxManus zzxManus;

    @Test
    void run() {
        String userPrompt = "论文各部分写作有什么顺序建议吗？";
        String result = zzxManus.run(userPrompt);
        System.out.println(result);

    }

}