package com.ec.svg.generator.app.dto;

// Generated: Wed May 08 11:33:10 AEST 2024

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


    @Override
    public String toString() {
        return "AuditDetailsDTO{" +
                "createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", createdTs=" + createdTs +
                ", modifiedTs=" + modifiedTs +
                ", version=" + version +
                ", isActive=" + isActive +
                '}';
    }
}
