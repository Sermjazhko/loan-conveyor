package com.gateway.integration;

import com.gateway.dto.LoanApplicationRequestDTO;
import com.gateway.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private static final String URL_APPLICATION = "/application";

    @Value("${integration.application.url}")
    private String urlApplication;

    private final RestTemplate restTemplate;

    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("URL: " + urlApplication + URL_APPLICATION);

        LoanOfferDTO[] loanOffers = restTemplate.postForObject(urlApplication + URL_APPLICATION,
                loanApplicationRequestDTO, LoanOfferDTO[].class);
        List<LoanOfferDTO> loanOfferList = Arrays.asList(Objects.requireNonNull(loanOffers));
        log.info("Received offers: {}", loanOfferList);
        return loanOfferList;
    }

    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("URL: " + urlApplication + URL_APPLICATION + "/offer");

        restTemplate.put(urlApplication + URL_APPLICATION + "/offer", loanOfferDTO);
        log.info("Apply offer: {}", loanOfferDTO);
    }
}
