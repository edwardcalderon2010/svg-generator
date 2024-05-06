package com.ec.svg.generator.app.model.domain;

import com.ec.svg.generator.app.interfaces.Moveable;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.path.ParameterContext;
import com.ec.svg.generator.app.model.domain.path.Point;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SVGPathD extends SVGAttribute implements Moveable {

    private static final Logger logger = LoggerFactory.getLogger(SVGPathD.class);


    @Getter
    private List<SVGPathSection> pathSections;

    public SVGPathD() {
        super(AttributeType.d);
    }

    public void addPathSection(SVGPathSection pathSection) {
        if (pathSections == null) {
            pathSections = new ArrayList<>();
        }

        pathSections.add(pathSection);
        updateValue();
    }

    private void updateValue() {
        String result = "";

        if (pathSections != null && pathSections.size() > 0) {
            result = pathSections.stream().map(SVGPathSection::toString).collect(Collectors.joining(" "));
        }

        this.value = result;
    }

    public Point getReferencePoint(ParameterContext paramContext) {
        Point result = getPathSections()
                .stream()
                .map(section -> section.getContainingCurve(paramContext))
                .filter(Objects::nonNull)
                .map(curve -> curve.getBezierCurvePoint(paramContext.getCurveCoordinate()))
                .reduce((a,b) -> a.compareReduce(paramContext,b))
                .orElse(null);

        return result;
    }

    @Override
    public void applyXOffset(BigDecimal xOffset) {
        logger.info("Calling applyXOffset");
        if (pathSections != null && pathSections.size() > 0) {
            pathSections.forEach(
                    elem -> elem.getPathCommands()
                            .stream()
                            .filter(Objects::nonNull)
                            .forEach(
                                    cmd -> cmd.getParameterList()
                                            .stream()
                                            .filter(Objects::nonNull)
                                            .forEach(param -> param.applyXOffset(xOffset))));
        }
        updateValue();
    }

    @Override
    public void applyYOffset(BigDecimal yOffset) {
        logger.info("Calling applyYOffset");
        if (pathSections != null && pathSections.size() > 0) {
            pathSections.forEach(
                    elem -> elem.getPathCommands()
                            .stream()
                            .filter(Objects::nonNull)
                            .forEach(
                                    cmd -> cmd.getParameterList()
                                            .stream()
                                            .filter(Objects::nonNull)
                                            .forEach(param -> param.applyYOffset(yOffset))));
        }
        updateValue();
    }
}