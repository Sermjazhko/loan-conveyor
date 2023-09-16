package com.gateway.model;

import com.gateway.enums.Gender;
import com.gateway.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Client data")
public class Client implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthday;

    private String email;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private String passport;

    private String employment;

    private String account;
}

