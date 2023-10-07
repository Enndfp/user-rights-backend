# 使用一个基础的 Java 8 镜像
FROM openjdk:8-jre-alpine

# 创建一个工作目录
WORKDIR /app

# 将本地的 JAR 文件复制到容器中
COPY docker/user-rights-backend-0.0.1-SNAPSHOT.jar /app/user-rights-backend.jar

# 设置容器启动命令
CMD ["java", "-jar", "user-rights-backend.jar", "--spring.profiles.active=prod"]
