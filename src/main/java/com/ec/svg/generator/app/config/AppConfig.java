package com.ec.svg.generator.app.config;

import com.ec.svg.generator.app.engine.Generator;
import com.ec.svg.generator.app.interfaces.PathService;
import com.ec.svg.generator.app.model.entity.PathRepository;
import com.ec.svg.generator.app.service.PathServiceImpl;
import com.ec.svg.generator.app.util.ResourceHelper;
import com.ec.svg.generator.app.util.SVGLoader;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
    @Bean
    public SVGResourceProperties svgResourceProperties() {
        return new SVGResourceProperties();
    }
    @Bean
    public ResourceHelper resourceHelper() {
        return new ResourceHelper();
    }

    @Bean
    public SVGLoader svgLoader() {
        return new SVGLoader(resourceHelper(),svgResourceProperties(), modelMapper());

    }

}
