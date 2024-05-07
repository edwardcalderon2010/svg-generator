package com.ec.svg.generator.app.dto;

// Generated: Tue May 07 13:54:22 AEST 2024

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
