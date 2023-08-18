package com.conveyor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public String toString() {
        return "\nPaymentScheduleElement{" +
                "number= " + number +
                ", date= " + date +
                ", totalPayment= " + totalPayment +
                ", interestPayment= " + interestPayment +
                ", debtPayment= " + debtPayment +
                ", remainingDebt= " + remainingDebt +
                '}';
    }
}
