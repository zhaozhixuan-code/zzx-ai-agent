# 使用官方OpenJDK 21镜像（推荐使用openjdk而不是java）
FROM openjdk:21-jdk-slim

# 设置元数据
LABEL authors="zhaozhixuan"
LABEL description="Spring Boot Application for AI Agent"

# 设置工作目录
WORKDIR /app

# 复制JAR文件到容器中（使用app.jar作为文件名）
COPY target/zzx-ai-agent-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口
EXPOSE 8126

# 设置JVM参数（优化内存和性能）
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

# 设置启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=8126"]