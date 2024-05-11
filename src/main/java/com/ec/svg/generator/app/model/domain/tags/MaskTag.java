package com.ec.svg.generator.app.model.domain.tags;

import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import com.ec.svg.generator.app.model.entity.Path;

import java.math.BigDecimal;

public class MaskTag extends SVGElement {

    public MaskTag() {
        super(TagName.mask.name());
    }

    private MaskTag(Builder builder) {
        this();
        setId(builder.maskId);

        PathTag childPath = builder.pathTag;

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

        public Builder pathTag(PathTag pathTag) {
            this.pathTag = pathTag;
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
