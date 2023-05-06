package com.alfonsoalmonte.ipinformation.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class AutoCreateKafkaConfig {

    @Value("${spring.kafka.template.default-topic}")
    String topicname;

    @Bean
    public NewTopic ipResponseEvent() {

        return TopicBuilder.name(topicname)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
