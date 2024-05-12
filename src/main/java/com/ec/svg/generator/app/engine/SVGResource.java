package com.ec.svg.generator.app.engine;

import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.model.domain.FontCharacter;
import com.ec.svg.generator.app.model.domain.LetterDefinition;
import com.ec.svg.generator.app.model.domain.tags.GroupTag;
import com.ec.svg.generator.app.model.domain.tags.MaskTag;
import com.ec.svg.generator.app.model.domain.tags.PathTag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static com.ec.svg.generator.app.model.domain.FontCharacter.unicodeKeyToChar;


public class SVGResource {

    private static final Logger logger = LoggerFactory.getLogger(SVGResource.class);

    public static final String SVG_TAG_HEADER="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 1400 1000\">";
    @Getter
    private Map<Integer, LetterDefinition> fontDefinitions = new LinkedHashMap<>();

    @Getter
    private Map<String, List<MaskTag>> fontMasks = new LinkedHashMap<>();

    @Getter
    private Map<Integer, FontCharacter> fontAlphabet = new HashMap<>();

    @Getter
    private Map<String, List<GroupTag>> fontGroupTags = new LinkedHashMap<>();
    private String svgCharSequence = "";

    private BigDecimal lineWidth = BigDecimal.ZERO;


    public void init(Set<LetterDefinition> letterAlphabet) {
        // Pre-load font alphabet and definitions
        letterAlphabet.forEach(def -> logger.info("Loading SVGResource with " + unicodeKeyToChar(def.getUnicodeKey())));
        letterAlphabet.forEach(def -> fontDefinitions.put(def.getUnicodeKey(), def));
        letterAlphabet.forEach(def -> fontAlphabet.put(def.getUnicodeKey(), def));

        // Pre-load font alphabet with blank space char
        // note: this is not a letter definition and therefore requires no group/mask tag
        // note: however it still contributes to total width of rendered tags
        fontAlphabet.put(FontCharacter.BLANK_SPACE.getUnicodeKey(), FontCharacter.BLANK_SPACE);

    }
    private BigDecimal calculateCharSequenceWidth() {
        BigDecimal charSeqWidth = BigDecimal.ZERO;

        if (svgCharSequence != null && svgCharSequence.length() > 0) {
            BigDecimal startCharMinX = BigDecimal.ZERO;
            BigDecimal currentCharSeqWidth = BigDecimal.ZERO;

            startCharMinX = fontAlphabet.get((int)svgCharSequence.charAt(0)).getFontWidthMinX();
            currentCharSeqWidth = svgCharSequence.chars()
                    .mapToObj(chr -> fontAlphabet.get(chr).getFontWidth())
                    .reduce(BigDecimal::add)
                    .get();
            logger.info(" ### Char sequence width: " + currentCharSeqWidth);
            charSeqWidth = currentCharSeqWidth.add(startCharMinX);
        }

        return charSeqWidth;
    }

    private BigDecimal getStartRenderX(FontCharacter fontChar) {
        BigDecimal renderX = BigDecimal.ZERO;

        if (svgCharSequence != null && svgCharSequence.length() > 0) {
            renderX = this.lineWidth.subtract(fontChar.getFontWidthMinX());
        }

        return renderX;
    }


    public void addChar(int unicodeKey) {


        if (fontAlphabet.containsKey(unicodeKey)) {
            FontCharacter fontChar = fontAlphabet.get(unicodeKey);

            BigDecimal renderX = getStartRenderX(fontChar);

            svgCharSequence = svgCharSequence.concat(String.valueOf((char)unicodeKey));

            this.lineWidth = calculateCharSequenceWidth();

            long charRepeat = svgCharSequence.chars()
                    .filter(chrInt -> Integer.valueOf(chrInt).equals(fontChar.getUnicodeKey()))
                    .count();

            logger.info("###### Rendering " + unicodeKeyToChar(fontChar.getUnicodeKey()) + " of " + svgCharSequence + " at " + renderX);

            if (fontDefinitions.containsKey(fontChar.getUnicodeKey())) {
                LetterDefinition letterDef = fontDefinitions.get(fontChar.getUnicodeKey());
                String charSeqKey = unicodeKeyToChar(fontChar.getUnicodeKey()) + charRepeat;
                BigDecimal yOffset = new BigDecimal("00.00");

                logger.info("## Generating masks/groups for " + charSeqKey);
                //fontDefinitions.put(fontChar.getUnicodeKey(), letterDef);
                fontMasks.put(charSeqKey, letterDef.generateMaskTags(charSeqKey, renderX, yOffset));
                fontGroupTags.put(charSeqKey,letterDef.generateGroupTags(charSeqKey, renderX,yOffset));

            }

        }

    }

    public String render() {

        logger.info("Rendering " + svgCharSequence);

        StringBuilder sb = new StringBuilder();
        sb.append(SVG_TAG_HEADER + "\n");
        sb.append("\t<defs>\n");

        fontDefinitions.forEach((k,v) -> sb.append(v.renderPathDefinitions() + "\n") );
        fontMasks.forEach((k,v) -> v.forEach(maskTag -> sb.append(maskTag.render() + "\n")) );


        sb.append("\t</defs>\n");

        sb.append("<g id=\"letters\">");
        fontGroupTags.forEach((k,v) -> v.forEach(groupTag -> sb.append(groupTag.render() + "\n")));
        sb.append("</g>");

        sb.append("\t</svg>\n");


        return sb.toString();
    }
}
