package com.ec.svg.generator.app.dto;

// Generated: Wed May 08 11:33:10 AEST 2024

import lombok.Getter;
import lombok.Setter;



public class PathDTO {


    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String glyphName;

    @Getter
    @Setter
    private String pathData;

    @Getter
    @Setter
    private Integer unicode;

    @Getter
    @Setter
    private Boolean isMask;

    @Getter
    @Setter
    private Integer sequence;

    @Getter
    @Setter
    private AuditDetailsDTO auditDetails;


    @Override
    public String toString() {
        return "PathDTO{" +
                "id=" + id +
                ", glyphName='" + glyphName + '\'' +
                ", pathData='" + pathData + '\'' +
                ", unicode=" + unicode +
                ", isMask=" + isMask +
                ", sequence=" + sequence +
                ", auditDetails=" + auditDetails +
                '}';
    }
}
