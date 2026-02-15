# ==================== 阶段1: 构建 ====================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# 先复制 Maven Wrapper 和 pom.xml，利用 Docker 缓存依赖层
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# 下载依赖（这一层会被缓存，除非 pom.xml 变化）
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# 复制源码并构建
COPY src/ src/
RUN ./mvnw package -DskipTests -B && \
    java -Djarmode=layertools -jar target/*.jar extract --destination extracted

# ==================== 阶段2: 运行 ====================
FROM eclipse-temurin:21-jre-alpine AS runtime

# 安全：创建非 root 用户
RUN addgroup -g 1000 app && adduser -u 1000 -G app -D app

WORKDIR /app

# 按变化频率从低到高复制分层，最大化缓存利用
COPY --from=builder /build/extracted/dependencies/ ./
COPY --from=builder /build/extracted/spring-boot-loader/ ./
COPY --from=builder /build/extracted/snapshot-dependencies/ ./
COPY --from=builder /build/extracted/application/ ./

USER app

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
