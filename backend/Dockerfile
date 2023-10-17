# 使用一个基础的容器镜像，通常选择一个包含 Java 运行环境的镜像
FROM openjdk:18-jdk-alpine

# 设置工作目录，可以根据需要修改
WORKDIR \app

# 复制构建好的 Spring Boot JAR 文件到容器中，这里假设你的应用被打包为一个可执行的 JAR 文件
COPY target/backend-0.0.1-SNAPSHOT.jar .

# 暴露应用运行的端口，根据你的 Spring Boot 项目配置来设置
EXPOSE 3000

# 启动应用，使用 java -jar 命令运行 Spring Boot 应用
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]  # 请替换成你的应用的 JAR 文件名