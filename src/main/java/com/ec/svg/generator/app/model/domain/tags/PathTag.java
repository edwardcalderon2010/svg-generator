package com.ec.svg.generator.app.model.domain.tags;

import com.ec.svg.generator.app.interfaces.Moveable;
import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.SVGAttribute;
import com.ec.svg.generator.app.util.PathHelper;
import com.ec.svg.generator.app.util.StringUtils;

import java.math.BigDecimal;

import static com.ec.svg.generator.app.model.domain.enums.AttributeType.stroke;
import static com.ec.svg.generator.app.model.domain.enums.AttributeType.strokeFill;
import static com.ec.svg.generator.app.model.domain.enums.AttributeType.strokeWidth;
import static com.ec.svg.generator.app.model.domain.enums.TagName.path;

public class PathTag extends SVGElement implements Moveable {

    public void setStrokeFill(String value) {
        this.addAttribute(new SVGAttribute(strokeFill,value));
    };

    public void setStroke(String value) {
        this.addAttribute(new SVGAttribute(stroke,value));
    };

    public void setStrokeWidth(String value) {
        this.addAttribute(new SVGAttribute(strokeWidth,value));
    };

    public PathTag() {
        super(path.name());
    }

    private PathTag(Builder builder) {
        this();

        setId(builder.tagId);
        addAttribute(PathHelper.parseGlyphPath(builder.pathCommandString));

        if (StringUtils.hasText(builder.pathStroke)) {
            setStroke(builder.pathStroke);
        }

        if (StringUtils.hasText(builder.pathStrokeFill)) {
            setStrokeFill(builder.pathStrokeFill);
        }

        if (StringUtils.hasText(builder.pathStrokeWidth)) {
            setStrokeWidth(builder.pathStrokeWidth);
        }

        if (StringUtils.hasText(builder.className)) {
            addClassName(builder.className);
        }
    }

    @Override
    public void applyXOffset(BigDecimal xOffset) {

        if (attributeMap != null && attributeMap.values().size() > 0) {
            attributeMap.values().stream()
                    .filter(elem -> elem instanceof Moveable)
                    .map(elem -> (Moveable)elem)
                    .forEach(moveable -> moveable.applyXOffset(xOffset));
        }
    }

    @Override
    public void applyYOffset(BigDecimal yOffset) {
        if (attributeMap != null && attributeMap.values().size() > 0) {
            attributeMap.values().stream()
                    .filter(elem -> elem instanceof Moveable)
                    .map(elem -> (Moveable)elem)
                    .forEach(moveable -> moveable.applyYOffset(yOffset));
        }
    }


//    <mask id="exc1" >
//        <path id="exc_mask_1" class="strokeMask part1"
//            fill="none" stroke="#fff" stroke-width="17"
//            d="M 656,583.5 C 634,673.5 605,723.5 610.75,743.75" />
//    </mask>

    public static class Builder {

        private String pathStroke;

        private String pathStrokeFill;

        private String pathStrokeWidth;

        private String pathCommandString;

        private String className;

        private String tagId;

        public Builder(String pathCommandString, String tagId) {
            this.pathCommandString = pathCommandString;
            this.tagId = tagId;
        }

        public Builder pathStroke(String pathStroke) {
            this.pathStroke = pathStroke;
            return this;
        }

        public Builder pathStrokeFill(String pathStrokeFill) {
            this.pathStrokeFill = pathStrokeFill;
            return this;
        }

        public Builder pathStrokeWidth(String pathStrokeWidth) {
            this.pathStrokeWidth = pathStrokeWidth;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }
        public PathTag build() {
            return new PathTag(this);
        }
    }

}
