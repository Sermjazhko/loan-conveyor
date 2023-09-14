package com.dossier.model;

import com.dossier.enums.CreditStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Credit data")
public class Credit implements Serializable {

    private Long id;

    private BigDecimal amount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    private String paymentSchedule;

    private Boolean insuranceEnable;

    private Boolean salaryClient;

    private CreditStatus creditStatus;
}
