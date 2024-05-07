package com.ec.svg.generator.app.util;

import com.ec.svg.generator.app.SvgGeneratorApplication;
import com.ec.svg.generator.app.config.SVGResourceProperties;
import com.ec.svg.generator.app.model.entity.PathRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class SVGLoader {

    private static final Logger logger = LoggerFactory.getLogger(SvgGeneratorApplication.class);

    private ResourceHelper resourceHelper;

    private SVGResourceProperties svgResourceProperties;

    @Autowired
    private PathRepository pathRepository;

    public SVGLoader(ResourceHelper resourceHelper, SVGResourceProperties svgResourceProperties) {
        this.resourceHelper = resourceHelper;
        this.svgResourceProperties = svgResourceProperties;
    }
    public void load() {
        logger.info("Loading XML from: " + svgResourceProperties.getLowerCaseSVGPath());
        String loadPath = svgResourceProperties.getLowerCaseSVGPath();
        resourceHelper.loadXMLResources(loadPath, pathRepository);

    }
}
