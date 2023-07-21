package com.conveyor.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistoryDTO {

    private Enum status;
    private LocalDateTime time;
    private Enum changeType;
}
