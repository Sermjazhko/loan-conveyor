package com.dossier.dto;

import com.dossier.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "information about mail")
public class EmailMessage {

    @Schema(description = "email address")
    private String address;
    @Schema(description = "theme")
    private ApplicationStatus theme;
    @Schema(description = "application id")
    private Long applicationId;
}
