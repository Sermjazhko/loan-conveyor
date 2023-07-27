package com.conveyor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleElement {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;

    @Override
    public String toString() {
        return "PaymentScheduleElement{" +
                "number= " + number +
                ", date= " + date +
                ", totalPayment= " + totalPayment +
                ", interestPayment= " + interestPayment +
                ", debtPayment= " + debtPayment +
                ", remainingDebt= " + remainingDebt + "\n" +
                '}';
    }
}
