package com.ec.svg.generator.app.engine;

import com.ec.svg.generator.app.model.domain.FontCharacter;
import com.ec.svg.generator.app.model.domain.LetterDefinition;
import com.ec.svg.generator.app.model.domain.tags.GroupTag;
import com.ec.svg.generator.app.model.domain.tags.MaskTag;
import com.ec.svg.generator.app.model.domain.text.TextBlock;
import com.ec.svg.generator.app.util.StringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.ec.svg.generator.app.model.domain.FontCharacter.unicodeKeyToChar;


public class SVGResource {

    private static final Logger logger = LoggerFactory.getLogger(SVGResource.class);

    public static final String SVG_TAG_HEADER="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 1500 1000\">";

    @Getter
    private Map<Integer, LetterDefinition> fontDefinitions = new LinkedHashMap<>();
    @Getter
    private Map<Integer, FontCharacter> fontAlphabet = new HashMap<>();

    @Getter
    private Map<String, List<MaskTag>> fontMasks = new LinkedHashMap<>();

    @Getter
    private Map<String, List<GroupTag>> fontGroupTags = new LinkedHashMap<>();
    //private String svgCharSequence = "";

    private TextBlock textBlock;


    public void init(Set<LetterDefinition> letterAlphabet, String inputString) {
        // Pre-load font alphabet and definitions
        letterAlphabet.forEach(def -> logger.info("Loading SVGResource with " + unicodeKeyToChar(def.getUnicodeKey())));
        letterAlphabet.forEach(def -> fontDefinitions.put(def.getUnicodeKey(), def));
        letterAlphabet.forEach(def -> fontAlphabet.put(def.getUnicodeKey(), def));

        // Pre-load font alphabet with blank space char
        // note: this is not a letter definition and therefore requires no group/mask tag
        // note: however it still contributes to total width of rendered tags
        fontAlphabet.put(FontCharacter.BLANK_SPACE.getUnicodeKey(), FontCharacter.BLANK_SPACE);

        textBlock = new TextBlock();
        List<String> words = Arrays.asList(inputString.split(" "));
        words.forEach(textBlock::addWord);

        textBlock.getTextLines().forEach(line -> logger.info("Got line: >" + line + "<"));

    }
    private BigDecimal calculateCharSequenceWidth(String inputString) {
        BigDecimal charSeqWidth = BigDecimal.ZERO;

        if (inputString != null && inputString.length() > 0) {
            BigDecimal startCharMinX = BigDecimal.ZERO;
            BigDecimal currentCharSeqWidth = BigDecimal.ZERO;

            startCharMinX = fontAlphabet.get((int)inputString.charAt(0)).getFontWidthMinX();
            currentCharSeqWidth = inputString.chars()
                    .mapToObj(chr -> fontAlphabet.get(chr).getFontWidth())
                    .reduce(BigDecimal::add)
                    .get();
            logger.info(" ### Char sequence width: " + currentCharSeqWidth);
            charSeqWidth = currentCharSeqWidth.add(startCharMinX);
        }

        return charSeqWidth;
    }

    private BigDecimal getStartRenderX(FontCharacter fontChar, BigDecimal currentLineWidth) {
        BigDecimal renderX = BigDecimal.ZERO;

        if (currentLineWidth.compareTo(BigDecimal.ZERO) > 0) {
            renderX = currentLineWidth.subtract(fontChar.getFontWidthMinX());
        }

        return renderX;
    }


    public void generate() {

        textBlock.getTextLines().forEach(line -> {
            StringBuilder workingString = new StringBuilder();
            line.chars().boxed().forEach(chr -> {
                addChar(chr, workingString.toString(), textBlock.getTextLines().indexOf(line));
                workingString.append(String.valueOf((char)chr.intValue()));
            });
        });

    }

    private void addChar(int unicodeKey, String targetString, int lineCount) {

        logger.info("####### Adding char " + unicodeKey + " to string >" + targetString + "< on line " + lineCount);

        if (fontAlphabet.containsKey(unicodeKey)) {
            FontCharacter fontChar = fontAlphabet.get(unicodeKey);

            BigDecimal renderX = getStartRenderX(fontChar, calculateCharSequenceWidth(targetString));

            targetString = targetString.concat(String.valueOf((char)unicodeKey));

            //this.lineWidth = calculateCharSequenceWidth(targetString);

            long charRepeat = targetString.chars()
                    .filter(chrInt -> Integer.valueOf(chrInt).equals(fontChar.getUnicodeKey()))
                    .count();

            logger.info("###### Rendering " + unicodeKeyToChar(fontChar.getUnicodeKey()) + " of " + targetString + " at " + renderX);

            if (fontDefinitions.containsKey(fontChar.getUnicodeKey())) {
                LetterDefinition letterDef = fontDefinitions.get(fontChar.getUnicodeKey());
                String charSeqKey = unicodeKeyToChar(fontChar.getUnicodeKey()) + charRepeat + "_" + lineCount;
                BigDecimal yOffset = new BigDecimal("322.00");
                BigDecimal yMod = new BigDecimal(lineCount);

                //logger.info("## Generating masks/groups for " + charSeqKey);
                fontMasks.put(charSeqKey, letterDef.generateMaskTags(charSeqKey, renderX, yOffset.multiply(yMod)));
                fontGroupTags.put(charSeqKey,letterDef.generateGroupTags(charSeqKey, renderX,yOffset.multiply(yMod)));

            }

        }

    }

    public String render() {

        logger.info("Rendering " + String.join("", textBlock.getTextLines()));

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
