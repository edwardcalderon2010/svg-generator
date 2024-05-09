package com.ec.svg.generator.app.util;

import com.ec.svg.generator.app.SvgGeneratorApplication;
import com.ec.svg.generator.app.config.SVGResourceProperties;
import com.ec.svg.generator.app.model.entity.PathRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class SVGLoader {

    private static final Logger logger = LoggerFactory.getLogger(SvgGeneratorApplication.class);

    private ResourceHelper resourceHelper;

    private SVGResourceProperties svgResourceProperties;

    private ModelMapper modelMapper;
    @Autowired
    private PathRepository pathRepository;

    public SVGLoader(ResourceHelper resourceHelper, SVGResourceProperties svgResourceProperties, ModelMapper modelMapper) {
        this.resourceHelper = resourceHelper;
        this.svgResourceProperties = svgResourceProperties;
        this.modelMapper = modelMapper;
    }
    public void load() {
        logger.info("Loading XML from: " + svgResourceProperties.getDefaultSVGPath());
        String loadPath = svgResourceProperties.getDefaultSVGPath();
        String masterResources = svgResourceProperties.getMasterXMLresource();
        //resourceHelper.loadXMLResources(loadPath, pathRepository);
        resourceHelper.loadFromXMLMasterFile(loadPath + masterResources, pathRepository, modelMapper);

    }
}
