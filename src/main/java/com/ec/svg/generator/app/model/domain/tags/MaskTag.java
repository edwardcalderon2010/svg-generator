package com.ec.svg.generator.app.model.domain.tags;

import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class MaskTag extends SVGElement {

    private static final Logger logger = LoggerFactory.getLogger(MaskTag.class);

    public MaskTag() {
        super(TagName.mask.name());
    }

    private MaskTag(Builder builder) {
        this();
        setId(builder.maskId);

        PathTag childPath = builder.pathTag;
        childPath.addClassName("cloneTest");

        if (childPath != null) {
            childPath.applyXOffset(builder.xOffset);
            childPath.applyYOffset(builder.yOffset);
            addChildElement(childPath);
        }
    }

    public static class Builder {
        private String maskId;
        private PathTag pathTag;

        private BigDecimal xOffset = BigDecimal.ZERO;

        private BigDecimal yOffset = BigDecimal.ZERO;

        public Builder maskId(String maskId) {
            this.maskId = maskId;
            return this;
        }

        public Builder pathTag(PathTag inputTag) {

            try {
                logger.info("Initiating clone on embedded PathTag: " + maskId);
                this.pathTag = (PathTag) inputTag.clone();
            } catch (CloneNotSupportedException cnse) {
                logger.info("PathTag clone failed " + cnse);
            }
            return this;
        }

        public Builder xOffset(BigDecimal xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder yOffset(BigDecimal yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public MaskTag build() {
            return new MaskTag(this);
        }

    }
}
