package com.ec.svg.generator.app.model.domain;

import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.enums.AxisPlane;
import com.ec.svg.generator.app.model.domain.enums.MathBound;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import com.ec.svg.generator.app.model.domain.path.ParameterContext;
import com.ec.svg.generator.app.model.domain.path.Point;
import com.ec.svg.generator.app.model.domain.path.Coordinate;
import com.ec.svg.generator.app.model.domain.tags.GroupTag;
import com.ec.svg.generator.app.model.domain.tags.PathTag;
import com.ec.svg.generator.app.model.domain.tags.MaskTag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ec.svg.generator.app.util.PathHelper.parseGlyphPath;

public class Letter {

    private static final Logger logger = LoggerFactory.getLogger(Letter.class);

    private static Integer ID_AUTO_INCREMENT = 1;
    private static final String PATH_LABEL_SUFFIX = "_path";
    private static final String MASK_LABEL_SUFFIX = "_mask";

    @Getter
    private Point maxXPoint;

    @Getter
    private Point minXPoint;

    @Getter
    private Point maxYPoint;

    @Getter
    private Point minYPoint;

    @Getter
    private BigDecimal fontWidth;

    public static int getIdAutoIncrement() {
        int result = ID_AUTO_INCREMENT;
        ID_AUTO_INCREMENT++;
        return result;
    }

    private HashMap<TagName, List<? extends SVGElement>> tagMap;
    public Letter(String name, List<String> letterPaths, List<String> letterMasks, BigDecimal xCoord, BigDecimal yCoord ) {

        tagMap = new HashMap<>();
        tagMap.put(TagName.path, new ArrayList<PathTag>());
        tagMap.put(TagName.mask, new ArrayList<MaskTag>());
        tagMap.put(TagName.g, new ArrayList<GroupTag>());

        int letterId = 0;
        String pathId = null;
        String pathMaskId = null;
        String maskId = null;

        for ( int pathIdx = 0; pathIdx < letterPaths.size(); pathIdx++) {
            letterId = getIdAutoIncrement();
            pathId = name + PATH_LABEL_SUFFIX + "_" + letterId;
            pathMaskId = name + MASK_LABEL_SUFFIX + "_" + letterId;
            maskId = name + letterId;

            // Generate the letter path (definition)
            PathTag path1 = parseGlyphPath(letterPaths.get(pathIdx), pathId);

            // Generate the path mask (nested path)
            PathTag pathMask1 = parseGlyphPath(letterMasks.get(pathIdx), pathMaskId);
            pathMask1.setStroke("#fff");
            pathMask1.setStrokeFill("none");
            pathMask1.setStrokeWidth("17");
            pathMask1.applyXOffset(xCoord);
            pathMask1.applyYOffset(yCoord);
            MaskTag mask1 = new MaskTag();
            mask1.setId(maskId);
            mask1.addChildElement(pathMask1);

            // Generate the letter group (nested use)
            GroupTag letterGroup = new GroupTag();
            letterGroup.setMask("url(#"+maskId+")");
            letterGroup.addUseElem("#"+pathId, xCoord, yCoord);

            addSVGDefinition(path1);
            addLetterMask(mask1);
            addLetterGroup(letterGroup);
        }

        calculateLetterWidth();

    }

    private void calculateLetterWidth() {
        // Letter width is the difference of xCoord between the min and max Params
        // whose yCoords are resting on 135

        Coordinate targetY = new Coordinate(new BigDecimal("135.00"), AxisPlane.Y);
        ParameterContext minXParamContainingY = new ParameterContext(MathBound.MIN, AxisPlane.X, targetY);
        ParameterContext maxXParamContainingY = new ParameterContext(MathBound.MAX, AxisPlane.X, targetY);


        Coordinate targetX = new Coordinate(new BigDecimal("50.00"), AxisPlane.X);
        ParameterContext minYParamContainingX = new ParameterContext(MathBound.MIN, AxisPlane.Y, targetX);
        ParameterContext maxYParamContainingX = new ParameterContext(MathBound.MAX, AxisPlane.Y, targetX);


        maxXPoint = getReferencePoint(maxXParamContainingY);
        logger.info("#### Got max X: " + maxXPoint.toString());

        minXPoint = getReferencePoint(minXParamContainingY);
        logger.info("#### Got min X: " + minXPoint.toString());

        maxYPoint = getReferencePoint(maxYParamContainingX);
        logger.info("#### Got max Y: " + maxYPoint.toString());

        minYPoint = getReferencePoint(minYParamContainingX);
        logger.info("#### Got min Y: " + minYPoint.toString());

        fontWidth = maxXPoint.getXValue().subtract(minXPoint.getXValue());
    }

    public Point getReferencePoint(ParameterContext paramContext) {
        Point resultPoint  = tagMap.get(TagName.path)
                .stream()
                .map(pathTag -> pathTag.getAttribute(AttributeType.d))
                .map(svgattr -> (SVGPathD)svgattr)
                .map(pathD -> pathD.getReferencePoint(paramContext))
                .filter(Objects::nonNull)
                .reduce((a,b) -> a.compareReduce(paramContext,b))
                .orElse(Point.ZERO);

        return resultPoint;
    }
    public void addClassName(TagName tagName, String className) {
        tagMap.get(tagName).forEach(elem -> elem.addClassName(className));
    }
    public void addSVGDefinition(PathTag tagElem) {
        List<PathTag> paths = (List<PathTag>) tagMap.get(TagName.path);
        paths.add(tagElem);
    }

    public void addLetterMask(MaskTag maskTag) {
        List<MaskTag> masks = (List<MaskTag>) tagMap.get(TagName.mask);
        masks.add(maskTag);
    }

    public void addLetterGroup(GroupTag groupTag) {
        List<GroupTag> groups = (List<GroupTag>) tagMap.get(TagName.g);
        groups.add(groupTag);
    }

    public String renderTagList(TagName tagName) {
        String result = "";
        List<? extends SVGElement> tempTagList = tagMap.get(tagName);

        if (tempTagList != null && tempTagList.size() > 0) {
            result = tempTagList.stream().map(SVGElement::render).collect(Collectors.joining(" "));
        }

        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("maxX:" + maxXPoint + ";\nminX:" + minXPoint + ";\nmaxY:" + maxYPoint + ";\nminY:" + minYPoint + "\n");
        sb.append("fontWidth:" + fontWidth.toString() + "\n");
        sb.append(renderTagList(TagName.path) + "\n");
        sb.append(renderTagList(TagName.mask) + "\n");
        sb.append(renderTagList(TagName.g) + "\n");

        return sb.toString();
    }
}
