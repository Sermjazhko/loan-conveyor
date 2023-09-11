package com.dossier.integration;

import com.dossier.model.Application;
import com.dossier.model.Client;
import com.dossier.model.Credit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    private static final String URL_DEAL_GET_APPLICATION = "/deal/get/application/";

    @Value("${integration.deal.url}")
    private String urlDeal;

    private final RestTemplate restTemplate;

    public Application getApplicationById(Long id) {
        log.info("URL: " + urlDeal + URL_DEAL_GET_APPLICATION + id);

        Application application = restTemplate.getForObject(urlDeal + URL_DEAL_GET_APPLICATION + id, Application.class);

        log.info("application: " + application);
        return application;
    }

}
