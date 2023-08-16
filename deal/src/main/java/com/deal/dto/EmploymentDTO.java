package com.deal.dto;

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
@EqualsAndHashCode
@Schema(description = "Employee Information")
public class EmploymentDTO {

    @Schema(description = "Employee status: self-employed/unemployed/business")
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    @Schema(description = "Position in the company: manager, middle manager, top manager")
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
