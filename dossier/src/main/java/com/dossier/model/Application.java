package com.dossier.model;

import com.dossier.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Schema(description = "Application data")
public class Application implements Serializable {

    private Long id;

    private Client client;

    private Credit credit;

    private ApplicationStatus applicationStatus;

    private Date creationDate;

    private String appliedOffer;

    private Date signDate;

    private String sesCode;

    private String statusHistory;
}
