package com.deal.model;

import com.deal.enums.ApplicationStatus;
import com.deal.enums.ChangeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Status history data, jsonb")
public class StatusHistory {

    private ApplicationStatus status;
    private Date date;
    private ChangeType changeType;
}
