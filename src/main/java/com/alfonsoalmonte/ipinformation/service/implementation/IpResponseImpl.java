package com.alfonsoalmonte.ipinformation.service.implementation;

import com.alfonsoalmonte.ipinformation.dto.IpRequest;
import com.alfonsoalmonte.ipinformation.message.IpRequestMessagePublish;
import com.alfonsoalmonte.ipinformation.model.IpResponse;
import com.alfonsoalmonte.ipinformation.repository.IpResponseRepository;
import com.alfonsoalmonte.ipinformation.service.IpResponseService;
import com.alfonsoalmonte.ipinformation.utils.AbstractIpRequestUtil;
import com.alfonsoalmonte.ipinformation.utils.DateTimeUtil;
import com.alfonsoalmonte.ipinformation.utils.DistanceUtil;
import com.alfonsoalmonte.ipinformation.utils.IpAddressValidatorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.cache.annotation.Cacheable;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Slf4j
public class IpResponseImpl extends AbstractIpRequestUtil implements IpResponseService {
    @Autowired
    private IpResponseRepository ipResponseRepository;
    @Autowired
    private final IpAddressValidatorUtil ipAddressValidatorUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    IpRequestMessagePublish ipRequestMessagePublish;

    public IpResponseImpl(RestTemplate restTemplate, IpAddressValidatorUtil ipAddressValidatorUtil) {
        super(restTemplate);
        this.ipAddressValidatorUtil = ipAddressValidatorUtil;
    }

    @Override
    public IpRequest findIpInfoByIpAddress(String ipAddress) throws JsonProcessingException {
        if(!ipAddressValidatorUtil.isValidIpAddress(ipAddress)){
            log.error("Invalid IP Address: {}", ipAddress);
            throw new IllegalArgumentException("La dirección IP no es válida.");
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + ipAddress + "/json/");
        ResponseEntity<IpResponse> response;

        //Validation and
        try {
           response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    IpResponse.class
            );
        }catch (RestClientException restClientException){
            log.error("Error in ip response - {}", restClientException.getMessage());
            throw new RuntimeException("Error retrieving Ip information");
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully ip response {}", response.getBody().getIp());

            IpResponse getIpResponse = new IpResponse();
            String ip = response.getBody().getIp();
            getIpResponse.setIp(ip);
            String region = response.getBody().getRegion();
            getIpResponse.setRegion(region);
            String countryCode = response.getBody().getCountry_code();
            getIpResponse.setCountry_code(countryCode);
            String countryName = response.getBody().getCountry_name();
            getIpResponse.setCountry_name(countryName);
            String languages = response.getBody().getLanguages();
            getIpResponse.setLanguages(languages);
            String currency = response.getBody().getCurrency();
            getIpResponse.setCurrency(currency);
            Double latitude = response.getBody().getLatitude();
            getIpResponse.setLatitude(latitude);
            Double longitude = response.getBody().getLongitude();
            getIpResponse.setLongitude(longitude);
            String timeZone = response.getBody().getTimezone();
            getIpResponse.setTimezone(timeZone);

            IpResponse saveIpResponse = this.saveIpResponse(getIpResponse);

            //Envio el mensaje a los microservicios que tienen asignado el topic ipRequest-topic
            ipRequestMessagePublish.sendIpAddressEvent(saveIpResponse);

            getIpResponse.setId_ip(getIpResponse.getId_ip());

            //Hora y fecha del pais request
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeZone));

            //Construyo el body a mostrar cuando se realice el Get
            IpRequest ipRequest = new IpRequest();
            ipRequest.setIp(ip + ", current date: " + now + " Country: " + countryName);
            ipRequest.setIsoCode(countryCode);
            ipRequest.setLanguages(languages);
            ipRequest.setCurrency(currency);

            String formattedTime = DateTimeUtil.getCurrentArgentinaTimeFormatted();

            //Hora Local del pais Argentina
            ipRequest.setTime(formattedTime);

            //Calcular distance estimada desde el ip del cliente hasta argentina
            double argLat = DistanceUtil.getARG_LATITUDE();
            double argLon = DistanceUtil.getARG_LONGITUDE();
            Double totalDistanceInvocation = DistanceUtil.calculateDistanceFromArgentina(latitude, longitude);
            totalDistanceInvocation = Math.round(totalDistanceInvocation * 100) / 100d;
            String estimatedDistance = totalDistanceInvocation + " kms (" + argLat + ", " + argLon + ") a (" + latitude + ", " + longitude + ")";

            ipRequest.setEstimatedDistance(estimatedDistance);

            return ipRequest;
        }
        log.error("Error in ip response - httpStatus was: {}", response.getStatusCode());
        throw new RuntimeException("Error retrieving Ip information");
    }

    @Override
    @Cacheable(cacheNames = "ipresponse", key = "#ipResponse.ip")
    public IpResponse saveIpResponse(IpResponse ipResponse) {
        log.info("SERVICE: Get Find By Ip: {}",ipResponse.getIp());
        return ipResponseRepository.save(ipResponse);
    }
}
