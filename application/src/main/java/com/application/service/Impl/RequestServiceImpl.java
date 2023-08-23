package com.application.service.Impl;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${deal.application.url}")
    private String URL_DEAL_APPLICATION = "";

    @Value("${deal.offer.url}")
    private String URL_DEAL_OFFER = "";

    @Override
    public List<LoanOfferDTO> getResultPostRequestOffer(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        log.info("Start POST request!" + URL_DEAL_APPLICATION);
        LoanOfferDTO[] rateResponse = restTemplate.postForObject(URL_DEAL_APPLICATION,
                loanApplicationRequestDTO, LoanOfferDTO[].class);
        log.info("End POST request!");

        List<LoanOfferDTO> loanOfferDTOS = Arrays.asList(rateResponse);

        return loanOfferDTOS;
    }

    @Override
    public boolean getResultPutRequestCalculation(LoanOfferDTO loanOfferDTO) {

        log.info("Start PUT request!" + URL_DEAL_OFFER);
        restTemplate.put(URL_DEAL_OFFER, loanOfferDTO);
        log.info("End PUT request!");

        return true;
    }
}
