package com.ec.svg.generator.app.model.domain;

import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.enums.SymbolicUnicodeChar;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import com.ec.svg.generator.app.model.domain.tags.GroupTag;
import com.ec.svg.generator.app.model.domain.tags.MaskTag;
import com.ec.svg.generator.app.model.domain.tags.PathTag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ec.svg.generator.app.util.PathHelper.*;

public class LetterDefinition extends FontCharacter {
    private static final Logger logger = LoggerFactory.getLogger(LetterDefinition.class);

    private static final String PATH_LABEL_SUFFIX = "_path";

    private static final String PATH_LABEL_CAP_SUFFIX = "_cap_path";
    private static final String MASK_LABEL_SUFFIX = "_mask";

    @Getter
    private List<PathTag> pathDefinitions = new ArrayList<>();
    private List<PathTag> maskDefinitions = new ArrayList<>();


    public static String getPathLabel(Integer unicodeKey) {
        String result = PATH_LABEL_SUFFIX;
        if (isUpperCaseCharacter(unicodeKey)) {
            result = PATH_LABEL_CAP_SUFFIX;
        }
        return result;
    }
    private LetterDefinition(Builder builder) {
        super(builder.unicodeKey);
        this.pathDefinitions = builder.pathDefinitions;
        this.maskDefinitions = builder.maskDefinitions;

        fontBoundsMinX = getMinXBounds(pathDefinitions);
        fontBoundsMaxX = getMaxXBounds(pathDefinitions);

        fontWidthMinX = getMinXReferencePointAtY(pathDefinitions);
        fontWidthMaxX = getMaxXReferencePointAtY(pathDefinitions);

        if (isUpperCaseCharacter(unicodeKey)) {

            switch (unicodeKeyToChar(unicodeKey)) {
                case "C":
                case "E":
                case "G":
                    fontWidthMinX = fontBoundsMinX;
                    break;
            }
        }

        // Reset fontminX to zero for special case char '!'
        if (isSymbolicCharacter(unicodeKey)) {
            fontWidthMinX = BigDecimal.ZERO;
            fontWidthMaxX = fontBoundsMaxX;
        }

        fontWidth = fontWidthMaxX.subtract(fontWidthMinX);

        logger.info("Calculated font width: " + fontWidth + " " + unicodeKeyToChar(this.getUnicodeKey(),Boolean.TRUE));
        logger.info("## Min X Width: " + fontWidthMinX);
        logger.info("## Max X Width: " + fontWidthMaxX);
        logger.info("## Min X Bounds: " + fontBoundsMinX);
        logger.info("## Max X Bounds: " + fontBoundsMaxX);
    }

    public String renderPathDefinitions() {
        String result = "";

        if (pathDefinitions.size() > 0) {
            result = pathDefinitions.stream().map(SVGElement::render).sorted().collect(Collectors.joining("\n"));
        }

        return result;
    }

    public List<MaskTag> generateMaskTags(String maskIdPrefix, BigDecimal xOffset, BigDecimal yOffset) {
        List<MaskTag> maskTags = new ArrayList<>();


        if (maskDefinitions.size() > 0) {
            final List<PathTag> clonedMasks = new ArrayList<>();
            //maskDefinitions.forEach();

            maskDefinitions.forEach(pathTag -> maskTags.add(
                    new MaskTag.Builder()
                            .maskId(maskIdPrefix + "_" + (maskDefinitions.indexOf(pathTag) + 1))
                            .xOffset(xOffset)
                            .yOffset(yOffset)
                            .pathTag(pathTag)
                            .build()));

            maskTags.forEach(maskTag -> maskTag.getChildElement(TagName.path)
                    .setId(maskIdPrefix + MASK_LABEL_SUFFIX + "_" + (maskTags.indexOf(maskTag) + 1)));
        }



        return maskTags;
    }

    public List<GroupTag> generateGroupTags(String maskIdPrefix, BigDecimal xOffset, BigDecimal yOffset) {
        List<GroupTag> groupTags = new ArrayList<>();
        if (maskDefinitions.size() > 0) {
            maskDefinitions.forEach(pathTag -> groupTags.add(
                    new GroupTag.Builder(maskIdPrefix + "_" + (maskDefinitions.indexOf(pathTag) + 1))
                            .childUseTag(xOffset,yOffset)
                            .useHref(unicodeKeyToChar(unicodeKey) + getPathLabel(unicodeKey) + "_" + (maskDefinitions.indexOf(pathTag) + 1))
                            .build()
            ));
        }
        return groupTags;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("fontWidth:" + fontWidth.toString() + "\n");
        sb.append(renderPathDefinitions() + "\n");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        logger.info("### EQUALS: ");
        return super.equals(obj);
    }


    public static class Builder {
        private Integer unicodeKey;

        private List<PathTag> pathDefinitions;

        private List<PathTag> maskDefinitions;

        public Builder(Integer unicodeKey) {
            pathDefinitions = new ArrayList<>();
            maskDefinitions = new ArrayList<>();
            this.unicodeKey = unicodeKey;
        }

        public Builder pathDefinitions(List<PathDTO> defs) {
            defs.forEach(pathDTO -> pathDefinitions.add(
                    new PathTag.Builder(pathDTO.getPathData(), pathDTO.getGlyphName())
                            .pathStroke(pathDTO.getPathStroke())
                            .pathStrokeFill(pathDTO.getPathStrokeFill())
                            .pathStrokeWidth(pathDTO.getPathStrokeWidth())
                            .className("letter_path")
                            .build()));

            return this;
        }

        public Builder maskDefinitions(List<PathDTO> defs) {
            defs.forEach(pathDTO -> maskDefinitions.add(
                    new PathTag.Builder(pathDTO.getPathData(), pathDTO.getGlyphName())
                            .pathStroke(pathDTO.getPathStroke())
                            .pathStrokeFill(pathDTO.getPathStrokeFill())
                            .pathStrokeWidth(pathDTO.getPathStrokeWidth())
                            .className("strokeMask")
                            .build()));

            return this;
        }

        public LetterDefinition build() {
            return new LetterDefinition(this);
        }
    }
}
