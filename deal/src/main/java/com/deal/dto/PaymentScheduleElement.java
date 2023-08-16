package com.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Monthly payment schedule")
public class PaymentScheduleElement {

    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    @Schema(description = "Interest payment")
    private BigDecimal interestPayment;
    @Schema(description = "Debt repayment")
    private BigDecimal debtPayment;
    @Schema(description = "Outstanding balance")
    private BigDecimal remainingDebt;
}
