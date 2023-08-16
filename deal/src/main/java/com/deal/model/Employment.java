package com.deal.model;

import com.deal.entities.EmploymentStatus;
import com.deal.entities.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Employment data, jsonb")
public class Employment {

    private EmploymentStatus employmentStatus;
    private String employerInn;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
