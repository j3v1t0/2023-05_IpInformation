package com.alfonsoalmonte.ipinformation.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractIpRequestUtil {
    @Value("${countriesinfo.base-url}")
    protected String baseUrl;

    protected final RestTemplate restTemplate;
}
