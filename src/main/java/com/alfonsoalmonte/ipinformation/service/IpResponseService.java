package com.alfonsoalmonte.ipinformation.service;

import com.alfonsoalmonte.ipinformation.dto.IpRequest;
import com.alfonsoalmonte.ipinformation.model.IpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IpResponseService {


    IpRequest findIpInfoByIpAddress(String ipAddress) throws JsonProcessingException;

    IpResponse saveIpResponse(IpResponse ipResponse);
}
