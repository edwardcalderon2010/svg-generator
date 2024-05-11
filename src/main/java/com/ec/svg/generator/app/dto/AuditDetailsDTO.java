package com.ec.svg.generator.app.dto;

// Generated: Fri May 10 13:58:32 AEST 2024

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


public class AuditDetailsDTO {


    @Getter
    @Setter
    private String createdBy;

    @Getter
    @Setter
    private String modifiedBy;

    @Getter
    @Setter
    private Timestamp createdTs;

    @Getter
    @Setter
    private Timestamp modifiedTs;

    @Getter
    @Setter
    private Integer version;

    @Getter
    @Setter
    private Boolean isActive;



}
