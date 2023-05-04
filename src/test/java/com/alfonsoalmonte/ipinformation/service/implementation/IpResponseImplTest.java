package com.alfonsoalmonte.ipinformation.service.implementation;

import com.alfonsoalmonte.ipinformation.utils.IpAddressValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IpResponseImplTest {
    @Mock
    private IpAddressValidator ipAddressValidator;
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testValidIpAddress() {
        String ipAddress = "192.168.1.1";
        boolean result = IpAddressValidator.isValidIpAddress(ipAddress);
        Assertions.assertTrue(result);
    }
    @Test
    public void testInvalidIpAddress() {
        String ipAddress = "400.168.1.300";
        boolean result = IpAddressValidator.isValidIpAddress(ipAddress);
        Assertions.assertFalse(result);
    }
}