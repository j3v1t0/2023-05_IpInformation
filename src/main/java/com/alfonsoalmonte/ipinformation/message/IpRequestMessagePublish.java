package com.alfonsoalmonte.ipinformation.message;

import com.alfonsoalmonte.ipinformation.model.IpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Component
public class IpRequestMessagePublish {
    @Value("${spring.kafka.template.default-topic}")
    String topicName;

    private Logger log = LoggerFactory.getLogger(IpRequestMessagePublish.class);
    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;

    public ListenableFuture<SendResult<Integer, String>> sendIpAddressEvent(IpResponse ipResponse) throws JsonProcessingException {
        Integer key = ipResponse.getId_ip();
        String value = objectMapper.writeValueAsString(ipResponse);
        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topicName);
        ListenableFuture<SendResult<Integer, String>> listenableFuture =kafkaTemplate.send(producerRecord);


        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                // TODO Auto-generated method stub
                try {
                    handleSuccess(key, value, result);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                // TODO Auto-generated method stub
                handleFailure(key, value, ex);

            }

        });

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
        List<Header> recordHeaders = List.of(new RecordHeader("deposit-subscription-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }


    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error: send message and the error is {}", ex.getMessage());
        try {

        } catch (Throwable throwable) {
            log.error("Error on OnFailure {}", throwable.getMessage());
        }
    }


    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result)
            throws JsonMappingException, JsonProcessingException {

        log.info("Message Sent Successfully for the key :{} and the value is {},partition is {}", key, value,
                result.getRecordMetadata().partition());

    }
}
