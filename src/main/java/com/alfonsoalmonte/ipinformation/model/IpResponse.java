package com.alfonsoalmonte.ipinformation.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Entity
@Table(name = "ip_response")
public class IpResponse implements Serializable {

    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ip;
    private String ip;
    private String region;
    private String country_code;
    private String country_name;
    private String languages;
    private Double latitude;
    private Double longitude;
    private String currency;
    private String timezone;
    private boolean reserved;

}
