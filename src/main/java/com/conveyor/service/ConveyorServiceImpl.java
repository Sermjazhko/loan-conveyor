package com.conveyor.service;

import com.conveyor.dto.LoanApplicationRequestDTO;
import com.conveyor.dto.LoanOfferDTO;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.Math.*;

@Service
public class ConveyorServiceImpl implements ConveyorService {
    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO)
            throws IOException {

        List<LoanOfferDTO> loanOfferDTOS = new ArrayList<>();
        FileInputStream file;
        Properties properties = new Properties();
        try {
            file = new FileInputStream("src/main/resources/config.properties");
            properties.load(file);
            Double baseRate = Double.parseDouble(properties.getProperty("base.rate"));
            Double insurance = Double.parseDouble(properties.getProperty("insurance"));
            file.close();
            Long id = 0L; //чисто в теории, наверное, id может потом из бд быть известно,
            // сейчас немного непонятно, как формируется, поэтому очень нубно

            Boolean isInsuranceEnabled = false, isSalaryClient = false;
            Double newRate;
            for (int i = 0; i < 3; i++) {
                newRate = rateChange(baseRate, isInsuranceEnabled, isSalaryClient);
                loanOfferDTOS.add(createOffer(newRate, loanApplicationRequestDTO.getAmount(),
                        loanApplicationRequestDTO.getTerm(), id, isInsuranceEnabled, isSalaryClient, insurance));
                id += 1;
                isInsuranceEnabled = isSalaryClient;
                isSalaryClient = !isSalaryClient;
            }
            newRate = rateChange(baseRate, true, true);
            loanOfferDTOS.add(createOffer(newRate, loanApplicationRequestDTO.getAmount(),
                    loanApplicationRequestDTO.getTerm(), id, true, true, insurance));
        } catch (IOException e) {
            throw new IOException("File not found or request for file is invalid");
        }
        return loanOfferDTOS;
    }

    private Double rateChange(Double rate, Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        Double rateNew = rate;
        if (isInsuranceEnabled) {
            rateNew -= 3;
        }
        if (isSalaryClient) {
            rateNew -= 1;
        }
        return rateNew;
    }

    private LoanOfferDTO createOffer(Double rate, BigDecimal amount, Integer term, Long id,
                                    Boolean isInsuranceEnabled, Boolean isSalaryClient,
                                    Double insurance) {
        Double requestedAmount = amount.doubleValue();
        if (isInsuranceEnabled) {
            requestedAmount += insurance;
        }
        Double interestRate = rate*0.01 / 12;
        Double annuityPayment = requestedAmount * (interestRate +
                interestRate/(pow(1+interestRate,term)-1));
        BigDecimal monthlyPayment = BigDecimal.valueOf(Math.round(annuityPayment));
        BigDecimal totalAmount = BigDecimal.valueOf(Math.round(annuityPayment * term));
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(id, BigDecimal.valueOf(requestedAmount),
                totalAmount, term, monthlyPayment, BigDecimal.valueOf(rate),
                isInsuranceEnabled, isSalaryClient);
        return loanOfferDTO;
    }
}
