package com.zzx.zzxaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool("", "", "", "");
        String s = pdfGenerationTool.generatePDF("text.pdf", "nihao测试123");
        System.out.println(s);
    }
}