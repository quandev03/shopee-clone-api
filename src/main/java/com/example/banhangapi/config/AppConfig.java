package com.example.banhangapi.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class AppConfig {
    @Value("${spring.kafka.host}")
    String host;
    @Value("${spring.kafka.port}")
    String portKafka;
    @Value("${spring.kafka.url}")
    String urlKafka;
    @Value("${spring.kafka.group}")
    String groupKafka;
    @Value("${spring.data.redis.port}")
    Integer redisPort;
    @Value("${spring.datasource.jwt.secret}")
    String jwtSecret;
    public enum UntilTime{
        MINI_SECOND,
        SECOND,
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR
    }
    public class Times{
        private final Long MINI_SECOND = 1L;
        private final Long SECOND = 1000L;
        private final Long MINUTE = 60*SECOND;
        private final Long HOUR = 60*MINUTE;
        private final Long DAY = 24*HOUR;
        private final Long WEEK = 7*DAY;
        private final Long MONTH = 30*DAY;
        private final Long YEAR = 365*DAY;
    }
    @Value("${spring.firebase.url}")
    String urlFirebase;
    @Value("${spring.firebase.bucketname}")
    String bucketName;

}
