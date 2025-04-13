# Dùng Tomcat image chính thức
FROM tomcat:9.0-jdk17

# Xóa ứng dụng mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file vào Tomcat
COPY /target/banhangapi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war