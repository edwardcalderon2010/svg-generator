package com.ec.svg.generator.app.model.entity;

// Generated: Wed May 08 11:33:03 AEST 2024

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Embeddable
public class AuditDetails {



    @Getter
    @Setter
    @Column(name="created_by" , nullable = false)
    private String createdBy;


    @Getter
    @Setter
    @Column(name="modified_by" )
    private String modifiedBy;


    @Getter
    @Setter
    @Column(name="created_ts" , nullable = false)
    private Timestamp createdTs;


    @Getter
    @Setter
    @Column(name="modified_ts" )
    private Timestamp modifiedTs;


    @Getter
    @Setter
    @Column(name="version" , nullable = false)
    private Integer version;


    @Getter
    @Setter
    @Column(name="active" , nullable = false)
    private Boolean isActive;


}
