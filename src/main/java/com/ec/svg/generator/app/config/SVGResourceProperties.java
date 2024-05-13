package com.ec.svg.generator.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="resources")
@PropertySource(value = "classpath:/resource-properties.yml", factory = YamlPropertySourceFactory.class)
public class SVGResourceProperties {


    @Getter
    @Setter
    private String defaultSVGPath;

    @Getter
    @Setter
    private String lowerCaseSVGPath;

    @Getter
    @Setter
    private String masterXMLresource;

    @Getter
    @Setter
    private String outputFile;

    @Getter
    @Setter
    private String htmlTemplatePath;

    @Getter
    @Setter
    private String htmlHeaderTemplate;

    @Getter
    @Setter
    private String htmlFooterTemplate;
}
