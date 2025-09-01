package com.zzx.zzxaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.zzx.zzxaiagent.constants.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.*;


/**
 * 文件读写工具类
 */
@Slf4j
public class FileOperationTool {
    /**
     * 文件保存目录
     */
    public static final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    static {
        // 静态初始化块，确保保存目录存在
        createDirectoryIfNotExists();
    }

    /**
     * 确保文件保存目录存在，如果不存在则创建
     */
    public static void createDirectoryIfNotExists() {
        File directory = new File(FILE_DIR);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                log.info("文件保存目录已创建: {}", FILE_DIR);
            } else {
                log.info("创建文件保存目录失败: {}", FILE_DIR);

            }
        }
    }


    /**
     * 读取文件内容
     *
     * @param fileName 文件名
     * @return 文件内容字符串，如果读取失败返回null
     */
    @Tool(description = "read content from a file")
    public String readFile(@ToolParam(description = "name of the file") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "read file error" + e.getMessage();
        }
    }

    /**
     * 写入内容到文件
     *
     * @param fileName 文件名
     * @param content  要写入的内容
     * @return 写入成功返回true，否则返回false
     */
    @Tool(description = "Write content to a file")
    public String writeFile(
            @ToolParam(description = "Name of the file to write") String fileName,
            @ToolParam(description = "Content to write to the file") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }


}
