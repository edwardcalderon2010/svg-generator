package com.ec.svg.generator.app.engine;

import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.interfaces.PathService;
import com.ec.svg.generator.app.model.domain.Letter;
import com.ec.svg.generator.app.model.domain.LetterDefinition;
import com.ec.svg.generator.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
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
            inputString.chars().forEach(elem -> fetchLetterByUnicode(elem, svgResource));
            logger.info(svgResource.render());

        }
    }


    private Letter fetchLetterByUnicode(Integer unicode, SVGResource svgResource) {
        Letter letter = null;
        //logger.info("Processing letter for char: " + unicode);
        List<PathDTO> letterPaths = pathService.getByUnicode(unicode);

        if (letterPaths != null && letterPaths.size() > 0) {
            //svgResource.addChar(unicode);
            //letterPaths.stream().forEach(path -> logger.info("Fetched: " + path.getGlyphName()));
            svgResource.addLetterDefinition( new LetterDefinition
                            .Builder(unicode)
                            .pathDefinitions(letterPaths.stream().filter(pathDTO -> !pathDTO.getIsMask()).toList())
                            .maskDefinitions(letterPaths.stream().filter(PathDTO::getIsMask).toList())
                            .build()
                    );
        }
        return null;
    }
}
