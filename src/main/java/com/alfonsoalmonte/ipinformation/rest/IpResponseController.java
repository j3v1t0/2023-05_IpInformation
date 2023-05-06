package com.alfonsoalmonte.ipinformation.rest;

import com.alfonsoalmonte.ipinformation.dto.IpRequest;
import com.alfonsoalmonte.ipinformation.message.IpRequestMessagePublish;
import com.alfonsoalmonte.ipinformation.model.IpResponse;
import com.alfonsoalmonte.ipinformation.service.IpResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("ipresponse/")
public class IpResponseController {
    @Autowired
    private IpResponseService ipResponseService;

    @GetMapping("{ipAddress}")
    public ResponseEntity<?> getIpResponse(@PathVariable String ipAddress) throws JsonProcessingException{
        try {
            IpRequest ipRequest = ipResponseService.findIpInfoByIpAddress(ipAddress);
            log.info("CONTROLLER: Get By Ip: {}", ipRequest.getIp());
            log.info("Producer sent in Microservice IpInformation");
            return ResponseEntity.ok(ipRequest);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Ip information");
        }
    }
}
