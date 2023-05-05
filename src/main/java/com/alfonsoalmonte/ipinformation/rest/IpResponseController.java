package com.alfonsoalmonte.ipinformation.rest;

import com.alfonsoalmonte.ipinformation.dto.IpRequest;
import com.alfonsoalmonte.ipinformation.model.IpResponse;
import com.alfonsoalmonte.ipinformation.service.IpResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ipresponse/")
public class IpResponseController {
    @Autowired
    private IpResponseService ipResponseService;

    @GetMapping("{ipAddress}")
    public ResponseEntity<?> getIpResponse(@PathVariable String ipAddress) throws JsonProcessingException{
        try {
            IpRequest ipRequest = ipResponseService.findIpInfoByIpAddress(ipAddress);
            return ResponseEntity.ok(ipRequest);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Ip information");
        }
    }
}
