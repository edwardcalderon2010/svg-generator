package com.ec.svg.generator.app.engine;

import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.model.domain.LetterDefinition;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import com.ec.svg.generator.app.model.domain.tags.GroupTag;
import com.ec.svg.generator.app.model.domain.tags.MaskTag;
import com.ec.svg.generator.app.util.PathHelper;
import com.ec.svg.generator.app.util.StringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DeferredImportSelector;

import java.math.BigDecimal;
import java.util.*;

import static com.ec.svg.generator.app.util.StringUtils.unicodeKeyToChar;

public class SVGResource {

    private static final Logger logger = LoggerFactory.getLogger(SVGResource.class);

    public static final String SVG_TAG_HEADER="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 1400 1000\">";
    @Getter
    private HashMap<Integer, List<PathDTO>> fontDefinitions = new HashMap<>();

    @Getter
    private HashMap<String, List<MaskTag>> fontMasks = new LinkedHashMap<>();

    @Getter
    private HashMap<Integer, LetterDefinition> letterMap = new HashMap<>();

    @Getter
    private Map<String, List<GroupTag>> fontGroupTags = new LinkedHashMap<>();
    private String svgCharSequence = "";

    public void addLetterDefinition(LetterDefinition letterDefinition) {
        letterMap.put(letterDefinition.getUnicodeKey(), letterDefinition);

        BigDecimal renderX = BigDecimal.ZERO;
        BigDecimal startCharMinX = BigDecimal.ZERO;
        BigDecimal currentCharSeqWidth = BigDecimal.ZERO;

        if (svgCharSequence != null && svgCharSequence.length() > 0) {
            startCharMinX = letterMap.get((int) svgCharSequence.charAt(0)).getFontWidthMinX();
            currentCharSeqWidth = svgCharSequence.chars()
                    .mapToObj(chr -> letterMap.get(Integer.valueOf(chr)).getFontWidth())
                    .reduce(BigDecimal::add)
                    .get();
            logger.info(" ### Char sequence width: " + currentCharSeqWidth);
            BigDecimal targetCharMinX = letterDefinition.getFontWidthMinX();
            renderX = currentCharSeqWidth.add(startCharMinX).subtract(targetCharMinX);

        }

        addChar(letterDefinition.getUnicodeKey());

        long charRepeat = svgCharSequence.chars()
                .filter(chrInt -> Integer.valueOf(chrInt).equals(letterDefinition.getUnicodeKey()))
                .count();


        //logger.info("Char rpt count for " + unicodeKeyToChar(letterDefinition.getUnicodeKey(),Boolean.TRUE) + "; " + charRepeat);

        logger.info("###### Rendering " + unicodeKeyToChar(letterDefinition.getUnicodeKey()) + " of " + svgCharSequence + " at " + renderX);
        String maskIdPrefix = unicodeKeyToChar(letterDefinition.getUnicodeKey()) + charRepeat;
        BigDecimal yOffset = new BigDecimal("00.00");

        fontMasks.put(maskIdPrefix, letterDefinition.generateMaskTags(maskIdPrefix, renderX, yOffset));

        fontGroupTags.put(maskIdPrefix,letterDefinition.generateGroupTags(maskIdPrefix, renderX,yOffset));

    }

    private void addChar(Integer unicode) {
        svgCharSequence = svgCharSequence.concat(unicodeKeyToChar(unicode,Boolean.TRUE));

    }
    public String render() {

        logger.info("Rendering " + svgCharSequence);

        StringBuilder sb = new StringBuilder();
        sb.append(SVG_TAG_HEADER + "\n");
        sb.append("\t<defs>\n");

        svgCharSequence.chars().forEach(chr -> sb.append(letterMap.get(chr).renderPathDefinitions() + "\n"));
        fontMasks.forEach((k,v) -> v.forEach(maskTag -> sb.append(maskTag.render() + "\n")) );


        sb.append("\t</defs>\n");

        sb.append("<g id=\"letters\">");
        fontGroupTags.forEach((k,v) -> v.forEach(groupTag -> sb.append(groupTag.render() + "\n")));
        sb.append("</g>");

        sb.append("\t</svg>\n");


        return sb.toString();
    }
}
