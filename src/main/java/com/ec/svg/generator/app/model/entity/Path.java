package com.ec.svg.generator.app.model.entity;

// Generated: Wed May 08 11:33:03 AEST 2024
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity(name="path")
public class Path {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name="id" , nullable = false)
    private Long id;


    @Getter
    @Setter
    @Column(name="glyph_name" , nullable = false)
    private String glyphName;


    @Getter
    @Setter
    @Column(name="path_data" , nullable = false,columnDefinition="TEXT")
    private String pathData;


    @Getter
    @Setter
    @Column(name="unicode" , nullable = false)
    private Integer unicode;


    @Getter
    @Setter
    @Column(name="is_mask" )
    private Boolean isMask;


    @Getter
    @Setter
    @Column(name="sequence" )
    private Integer sequence;


    @Getter
    @Setter
    private AuditDetails auditDetails;

}
