package com.dossier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Loan offer")
public class LoanOfferDTO {

    @Schema(description = "Loan application id", example = "1")
    private Long applicationId;
    @Schema(description = "Requested loan amount", example = "20000.00")
    private BigDecimal requestedAmount;
    @Schema(description = "Amount including insurance", example = "30000.00")
    private BigDecimal totalAmount;
    @Schema(description = "Loan term", example = "12")
    private Integer term;
    @Schema(description = "Monthly loan payment", example = "12342.12")
    private BigDecimal monthlyPayment;
    @Schema(description = "Loan rate", example = "12.0")
    private BigDecimal rate;
    @Schema(description = "Checking if the client has insurance", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Checking if the client has a salary card of this bank", example = "false")
    private Boolean isSalaryClient;
}
