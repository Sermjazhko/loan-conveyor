package com.gateway.dto;

import com.gateway.enums.Gender;
import com.gateway.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Finish registration request")
public class FinishRegistrationRequestDTO {

    @Schema(description = "Gender", example = "MALE")
    private Gender gender;
    @Schema(description = "Marital Status", example = "MARRIED")
    private MaritalStatus maritalStatus;
    @Schema(description = "Dependent Amount", example = "1")
    private Integer dependentAmount;
    @Schema(description = "Passport Issue Date", example = "2012-12-10")
    private LocalDate passportIssueDate;
    @Schema(description = "passport Issue Brach", example = "someone")
    private String passportIssueBrach;
    @Schema(description = "employment")
    private EmploymentDTO employment;
    @Schema(description = "account", example = "12364542")
    private String account;
}
