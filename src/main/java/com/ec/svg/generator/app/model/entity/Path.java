package com.ec.svg.generator.app.model.entity;

// Generated: Tue May 07 13:54:16 AEST 2024
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity(name="path")
public class Path {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name="id")
    private Long id;


    @Getter
    @Setter
    @Column(name="glyph_name")
    private String glyphName;


    @Getter
    @Setter
    @Column(name="path_data",columnDefinition="TEXT")
    private String pathData;


    @Getter
    @Setter
    @Column(name="is_lower_case")
    private Boolean isIsLowerCase;


    @Getter
    @Setter
    @Column(name="is_symbol")
    private Boolean isIsSymbol;


    @Getter
    @Setter
    @Column(name="source")
    private String source;


    @Getter
    @Setter
    private AuditDetails auditDetails;



}
