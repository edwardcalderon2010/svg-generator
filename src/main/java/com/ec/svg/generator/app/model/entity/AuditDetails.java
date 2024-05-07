package com.ec.svg.generator.app.model.entity;

// Generated: Tue May 07 13:54:16 AEST 2024

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Embeddable
public class AuditDetails {



    @Getter
    @Setter
    @Column(name="created_by")
    private String createdBy;


    @Getter
    @Setter
    @Column(name="modified_by")
    private String modifiedBy;


    @Getter
    @Setter
    @Column(name="created_ts")
    private Timestamp createdTs;


    @Getter
    @Setter
    @Column(name="modified_ts")
    private Timestamp modifiedTs;


    @Getter
    @Setter
    @Column(name="version")
    private Integer version;


    @Getter
    @Setter
    @Column(name="active")
    private Boolean isActive;



}
