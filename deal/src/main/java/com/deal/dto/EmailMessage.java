package com.deal.dto;

import com.deal.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "information about mail")
public class EmailMessage {

    @Schema(description = "email address", example = "example@mail.ru")
    private String address;
    @Schema(description = "theme", example = "Example")
    private ApplicationStatus theme;
    @Schema(description = "application id", example = "1")
    private Long applicationId;
}
