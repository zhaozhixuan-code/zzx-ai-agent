package com.zzx.zzxaiagent.App;

import com.zzx.zzxaiagent.advisor.MyLoggerAdvisor;
import com.zzx.zzxaiagent.advisor.ProhibitedWordAdvisor;
import com.zzx.zzxaiagent.baidu.MyQueryTransformer;
import com.zzx.zzxaiagent.chatmemory.FileBasedChatMemory;
import com.zzx.zzxaiagent.chatmemory.MysqlChatMemory;
import com.zzx.zzxaiagent.constants.TranslationConstants;
import com.zzx.zzxaiagent.rag.QueryRewriter;
import com.zzx.zzxaiagent.rag.WriteAppRagCustomAdvisorFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Component
@Slf4j
public class WriteApp {


    // 本地知识库 + 内存向量存储
    @Resource
    private VectorStore writeAppVectorStore;

    // 阿里云知识库
    @Resource
    private Advisor writeAppRagCloudAdvisor;

    // 本地知识库 + pgVector向量存储
    @Resource
    private VectorStore pgVectorVectorStore;

    // 查询重写器
    @Resource
    private QueryRewriter queryRewriter;

    // 查询翻译器
    @Resource
    private MyQueryTransformer myQueryTransformer;

    // 工具注入
    @Resource
    private ToolCallback[] allTools;


    // 客户端
    private final ChatClient chatClient;

    // 系统提示词
    private final String SYSTEM_PROMPT;


    public WriteApp(ChatModel dashscopeChatModel, MysqlChatMemory mysqlChatMemory) {
        // 加载系统prompt
        SystemPromptTemplate systemPrompt = new SystemPromptTemplate(new ClassPathResource("prompts/system-message.st"));
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "AI写作大师");
        this.SYSTEM_PROMPT = systemPrompt.render(variables);

        /* // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        FileBasedChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 初始化基于内存的对话记忆
        MessageWindowChatMemory InChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build(); */

        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(mysqlChatMemory).build(),
                        // 自定义日志拦截器
                        new MyLoggerAdvisor(),
                        // 注册违规词拦截器
                        new ProhibitedWordAdvisor()
                        /* ,
                        // Re2拦截器
                        new ReReadingAdvisor() */
                )
                .build();
    }


    /**
     * AI 基础对话，支持多轮对话记忆
     *
     * @param message 用户的提示词
     * @param chatId  对话id
     * @return AI 的回复
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        return content;
    }


    record WriteReport(String title, List<String> suggestions) {

    }

    /**
     * AI 报告功能，结构化输出
     *
     * @param message
     * @param chatId
     * @return
     */
    public WriteReport doChatWithReport(String message, String chatId) {
        WriteReport writeReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成写作结果，标题为{用户名}的写作，结果为建议列表")
                // 对话时动态设定拦截器参数，比如指定对话记忆的 id 和长度
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .user(message)
                .call()
                .entity(WriteReport.class);
        // String content = chatResponse.getResult().getOutput().getText();

        return writeReport;
    }

    /**
     * 基于本地知识库问答
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {

        // 执行查询翻译器
        message = myQueryTransformer.doQueryTranslation(message, TranslationConstants.ZH);

        // 执行查询重写器
        message = queryRewriter.doQueryRewrite(message);

        ChatResponse chatResponse = chatClient
                .prompt()
                // 对话时动态设定拦截器参数，比如指定对话记忆的 id 和长度
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 使用本地知识配置
                .advisors(new QuestionAnswerAdvisor(writeAppVectorStore))
                // 使用 RAG 基于阿里云知识库
                // .advisors(writeAppRagCloudAdvisor)
                // 应用 RAG 基于 pgVector + 本地知识库
                // .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 应用自定义的 RAG 检索增强服务（文档查询器+上下文增强）
                // .advisors(
                //         WriteAppRagCustomAdvisorFactory.doCreate(
                //                 writeAppVectorStore, "日常"
                //         )
                // // )
                // .user(message)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        return content;
    }




    public String doChatWithTools(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .toolCallbacks(allTools)
                .user(message)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);

        return content;
    }
}
