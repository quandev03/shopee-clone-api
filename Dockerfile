# Bước 1: Sử dụng Maven để build ứng dụng
FROM maven:3.8-openjdk-17-slim AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ mã nguồn vào trong container
COPY . .

# Build ứng dụng Spring Boot (war)
RUN mvn clean install -DskipTests

# Bước 2: Sử dụng image Tomcat để chạy ứng dụng WAR
FROM tomcat:10-jdk17-openjdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /usr/local/tomcat/webapps

# Copy file WAR đã build từ bước trước vào thư mục webapps của Tomcat
COPY --from=build /app/target/banhangapi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose cổng mà Tomcat chạy trên đó
EXPOSE 8080

# Lệnh để chạy Tomcat
CMD ["catalina.sh", "run"]