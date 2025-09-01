package com.zzx.zzxaiagent.tools;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String id = UUID.randomUUID().toString() + ".jpg";
        String string = resourceDownloadTool.downloadResource("https://images.xxapi.cn/images/meinv/idi.jpg", id);
        System.out.println(string);
    }
}