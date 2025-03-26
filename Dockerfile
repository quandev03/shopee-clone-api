# Bước 1: Tạo image build
FROM maven:3.8.1-openjdk-21-slim AS build

# Cài đặt thư mục làm việc
WORKDIR /app

# Sao chép pom.xml và tải phụ thuộc
COPY pom.xml .
RUN mvn dependency:go-offline

# Sao chép mã nguồn
COPY src /app/src

# Biên dịch ứng dụng thành file WAR
RUN mvn clean package -DskipTests

# Bước 2: Tạo image cuối cùng
FROM openjdk:21-slim

# Tạo thư mục trong container
WORKDIR /app

# Sao chép file WAR vào container
COPY --from=build /app/target/*.war /app/app.war

# Mở cổng 8080
EXPOSE 8080

# Chạy ứng dụng Spring Boot với file WAR
ENTRYPOINT ["java", "-jar", "/app/app.war"]