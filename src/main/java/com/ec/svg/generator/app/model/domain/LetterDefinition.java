package com.ec.svg.generator.app.model.domain;

import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import com.ec.svg.generator.app.model.domain.tags.GroupTag;
import com.ec.svg.generator.app.model.domain.tags.MaskTag;
import com.ec.svg.generator.app.model.domain.tags.PathTag;
import com.ec.svg.generator.app.util.StringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ec.svg.generator.app.util.PathHelper.*;
import static com.ec.svg.generator.app.util.StringUtils.unicodeKeyToChar;

public class LetterDefinition {
    private static final Logger logger = LoggerFactory.getLogger(LetterDefinition.class);

    private static final String PATH_LABEL_SUFFIX = "_path";
    private static final String MASK_LABEL_SUFFIX = "_mask";

    @Getter
    private final Integer unicodeKey;
    @Getter
    private BigDecimal fontWidth;

    @Getter
    private BigDecimal fontWidthMinX;

    @Getter
    private BigDecimal fontWidthMaxX;

    @Getter
    private BigDecimal fontBoundsMinX;

    @Getter
    private BigDecimal fontBoundsMaxX;

    private List<PathTag> pathDefinitions = new ArrayList<>();
    private List<PathTag> maskDefinitions = new ArrayList<>();

    private LetterDefinition(Builder builder) {
        this.unicodeKey = builder.unicodeKey;
        this.pathDefinitions = builder.pathDefinitions;
        this.maskDefinitions = builder.maskDefinitions;

        fontBoundsMinX = getMinXBounds(pathDefinitions);
        fontBoundsMaxX = getMaxXBounds(pathDefinitions);

        fontWidthMinX = getMinXReferencePointAtY(pathDefinitions);
        fontWidthMaxX = getMaxXReferencePointAtY(pathDefinitions);
        fontWidth = fontWidthMaxX.subtract(fontWidthMinX);
        logger.info("Calculated font width: " + fontWidth + " " + StringUtils.unicodeKeyToChar(this.getUnicodeKey(),Boolean.TRUE));
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

    public String renderMaskDefinitions() {
        String result = "";

        if (maskDefinitions.size() > 0) {
            result = maskDefinitions.stream().map(SVGElement::render).sorted().collect(Collectors.joining("\n"));
        }

        return result;
    }

    public List<MaskTag> generateMaskTags(String maskIdPrefix, BigDecimal xOffset, BigDecimal yOffset) {
        List<MaskTag> maskTags = new ArrayList<>();

        if (maskDefinitions.size() > 0) {

            maskDefinitions.forEach(pathTag -> maskTags.add(
                    new MaskTag.Builder()
                            .maskId(maskIdPrefix + "_" + (maskDefinitions.indexOf(pathTag) + 1))
                            .xOffset(xOffset)
                            .yOffset(yOffset)
                            .pathTag(pathTag)
                            .build()));
        }

        maskTags.forEach(maskTag -> maskTag.getChildElement(TagName.path)
                .setId(maskIdPrefix + MASK_LABEL_SUFFIX + "_" + (maskTags.indexOf(maskTag) + 1)));


        return maskTags;
    }

    public List<GroupTag> generateGroupTags(String maskIdPrefix, BigDecimal xOffset, BigDecimal yOffset) {
        List<GroupTag> groupTags = new ArrayList<>();
//        GroupTag letterGroup = new GroupTag();
//        letterGroup.setMask("url(#"+maskId+")");
//        letterGroup.addUseElem("#"+pathId, xCoord, yCoord);

        if (maskDefinitions.size() > 0) {
            maskDefinitions.forEach(pathTag -> groupTags.add(
                    new GroupTag.Builder(maskIdPrefix + "_" + (maskDefinitions.indexOf(pathTag) + 1))
                            .childUseTag(xOffset,yOffset)
                            .useHref(unicodeKeyToChar(unicodeKey) + PATH_LABEL_SUFFIX + "_" + (maskDefinitions.indexOf(pathTag) + 1))
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