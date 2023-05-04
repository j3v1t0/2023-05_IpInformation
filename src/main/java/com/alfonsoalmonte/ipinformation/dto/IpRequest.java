package com.alfonsoalmonte.ipinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IpRequest {
    private String ip;
    private String isoCode;
    private String languages;
    private String currency;
    private String hora;
    private String estimatedDistance;
}
