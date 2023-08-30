package com.application.integration;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private static final String URL_DEAL_APPLICATION = "/deal/application";

    private static final String URL_DEAL_OFFER = "/deal/offer";

    @Value("${integration.deal.url}")
    private String urlDeal;

    private final RestTemplate restTemplate;

    public List<LoanOfferDTO> createLoanApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        LoanOfferDTO[] loanOffers = restTemplate.postForObject(urlDeal + URL_DEAL_APPLICATION,
                loanApplicationRequestDTO, LoanOfferDTO[].class);
        log.info("Received offers: {}", (Object) loanOffers);
        return Arrays.asList(loanOffers);
    }

    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        restTemplate.put(urlDeal + URL_DEAL_OFFER, loanOfferDTO);
        log.info("Apply offer: {}", loanOfferDTO);
    }
}
