package com.alfonsoalmonte.ipinformation.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpAddressValidator {
    public static boolean isValidIpAddress(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            return inetAddress.getHostAddress().equals(ip);
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
