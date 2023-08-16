package com.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Application status history")
public class ApplicationStatusHistoryDTO {

    private Enum status;
    private LocalDateTime time;
    private Enum changeType;
}
