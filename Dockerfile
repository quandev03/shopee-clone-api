# Sử dụng image chính thức của OpenJDK làm base image
FROM openjdk:11-jre-slim

# Cài đặt biến môi trường cho tên ứng dụng
ARG JAR_FILE=target/*.jar

# Copy file JAR vào container
COPY ${JAR_FILE} app.jar

# Cấu hình port mà ứng dụng sẽ lắng nghe
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]