package com.example.banhangapi.kafka;

import com.example.banhangapi.api.service.implement.ProductServiceImple;
import com.example.banhangapi.redis.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private ProductServiceImple produceService;


    @KafkaListener(topics = "techmaster", groupId = "techmaster")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }





}
