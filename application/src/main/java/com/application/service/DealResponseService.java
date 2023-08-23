package com.application.service;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DealResponseService {

    private final RestTemplate restTemplate;

    @Value("${integration.deal.url}")
    private String URL_DEAL;

    private static final String URL_DEAL_APPLICATION = "/deal/application";

    private static final String URL_DEAL_OFFER = "/deal/offer";

    public DealResponseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<LoanOfferDTO> getResultPostRequestOffer(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        log.info("Start POST request!" + URL_DEAL + URL_DEAL_APPLICATION);
        LoanOfferDTO[] rateResponse = restTemplate.postForObject(URL_DEAL + URL_DEAL_APPLICATION,
                loanApplicationRequestDTO, LoanOfferDTO[].class);
        log.info("End POST request!");

        List<LoanOfferDTO> loanOfferDTOS = Arrays.asList(rateResponse);

        return loanOfferDTOS;
    }

    public boolean getResultPutRequestCalculation(LoanOfferDTO loanOfferDTO) {

        log.info("Start PUT request!" + URL_DEAL + URL_DEAL_OFFER);
        restTemplate.put(URL_DEAL + URL_DEAL_OFFER, loanOfferDTO);
        log.info("End PUT request!");

        return true;
    }
}
