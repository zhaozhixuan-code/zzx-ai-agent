package com.zzx.zzxaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileOperationToolTest {


    @Test
    void writeFile() {
        FileOperationTool tool = new FileOperationTool();
        String string = tool.writeFile("hello.txt", "hello");
        System.out.println(string);

    }

    @Test
    void readFile() {
        FileOperationTool tool = new FileOperationTool();
        String string = tool.readFile("hello.txt");
        System.out.println(string);
    }


}