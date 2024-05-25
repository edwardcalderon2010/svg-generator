package com.ec.svg.generator.app.engine;

import com.ec.svg.generator.app.config.SVGResourceProperties;
import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.interfaces.PathService;
import com.ec.svg.generator.app.model.domain.FontCharacter;
import com.ec.svg.generator.app.model.domain.LetterDefinition;
import com.ec.svg.generator.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Generator {

    private static final Logger logger = LoggerFactory.getLogger(Generator.class);

    private PathService pathService;

    private SVGResourceProperties svgResourceProperties;


    public Generator(PathService pathService, SVGResourceProperties svgResourceProperties)
    {
        this.pathService = pathService;
        this.svgResourceProperties = svgResourceProperties;
    }

    public void generateSVGFromString(String inputString) {

        if (StringUtils.hasText(inputString)) {
            logger.info("Generating SVG from: " + inputString);
            SVGResource svgResource = new SVGResource(svgResourceProperties);
            svgResource.init(assembleLetterDefinitionAlphabet(inputString), inputString);
            svgResource.generate();
            //inputString.chars().forEach(svgResource::addChar);
            //logger.info(svgResource.render());

            File outputFile = new File(svgResourceProperties.getDefaultSVGPath().concat(svgResourceProperties.getOutputFile()));
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write(StringUtils.prettyFormat(svgResource.render(), 4));
                //writer.write(svgResource.render());
                writer.flush();
                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }

    public String generateSVGOutput(String inputString) {
        String result = "";
        if (StringUtils.hasText(inputString)) {
            logger.info("Generating SVG from: " + inputString);
            SVGResource svgResource = new SVGResource(svgResourceProperties);
            svgResource.init(assembleLetterDefinitionAlphabet(inputString), inputString);
            svgResource.generate();
            //result = StringUtils.prettyFormat(svgResource.render(), 4);
            result = svgResource.render();
        }
        return result;
    }

    public String generateSVGEmbedded(String inputString) {
        String result = "";
        if (StringUtils.hasText(inputString)) {
            logger.info("Generating SVG from: " + inputString);
            SVGResource svgResource = new SVGResource(svgResourceProperties);
            svgResource.init(assembleLetterDefinitionAlphabet(inputString), inputString);
            svgResource.generate();
            //result = StringUtils.prettyFormat(svgResource.render(), 4);
            result = svgResource.renderHTML();
        }
        return result;
    }

    private Set<LetterDefinition> assembleLetterDefinitionAlphabet(String inputString) {

        Set<Integer> unicodeSet = new HashSet<>();
        inputString.chars().forEach(unicodeSet::add);

        Set<LetterDefinition> alphabet = new LinkedHashSet<>();

        unicodeSet.stream()
                .filter(unicode -> !FontCharacter.isWhitespace(unicode))
                .forEach(unicode -> {
                    List<PathDTO> paths = pathService.getByUnicode(unicode);
                    if (paths != null && paths.size() > 0) {
                        alphabet.add(new LetterDefinition
                                .Builder(unicode)
                                .pathDefinitions(paths.stream().filter(pathDTO -> !pathDTO.getIsMask()).toList())
                                .maskDefinitions(paths.stream().filter(PathDTO::getIsMask).toList())
                                .build());
                    }
                });

        return alphabet;
    }

}
