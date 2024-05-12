package com.ec.svg.generator.app.engine;

import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.interfaces.PathService;
import com.ec.svg.generator.app.model.domain.FontCharacter;
import com.ec.svg.generator.app.model.domain.LetterDefinition;
import com.ec.svg.generator.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Generator {

    private static final Logger logger = LoggerFactory.getLogger(Generator.class);

    private PathService pathService;


    public Generator(PathService pathService) {
        this.pathService = pathService;
    }

    public void generateSVGFromString(String inputString) {

        if (StringUtils.hasText(inputString)) {
            logger.info("Generating SVG from: " + inputString);
            SVGResource svgResource = new SVGResource();
            svgResource.init(assembleLetterDefinitionAlphabet(inputString), inputString);
            svgResource.generate();
            //inputString.chars().forEach(svgResource::addChar);
            logger.info(svgResource.render());

        }
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

//    private void fetchLetterByUnicode(Integer unicode, SVGResource svgResource) {
//        //logger.info("Processing letter for char: " + unicode);
//        List<PathDTO> letterPaths = pathService.getByUnicode(unicode);
//
//        if (letterPaths != null && letterPaths.size() > 0) {
//            //svgResource.addChar(unicode);
//            //letterPaths.stream().forEach(path -> logger.info("Fetched: " + path.getGlyphName()));
//            svgResource.addFontChar( new LetterDefinition
//                            .Builder(unicode)
//                            .pathDefinitions(letterPaths.stream().filter(pathDTO -> !pathDTO.getIsMask()).toList())
//                            .maskDefinitions(letterPaths.stream().filter(PathDTO::getIsMask).toList())
//                            .build()
//                    );
//        }
//    }
}
