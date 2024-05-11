package com.ec.svg.generator.app.model.domain.tags;

import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.SVGAttribute;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.enums.TagName;

import java.math.BigDecimal;

public class GroupTag extends SVGElement {

    public GroupTag() {
        super(TagName.g.name());
    }

    private GroupTag(Builder builder) {
        this();
        setMask(builder.maskUrl);
        UseTag tempTag = builder.childUseTag;
        tempTag.addAttribute(new SVGAttribute(AttributeType.href, "#"+builder.useHref));
        addChildElement(tempTag);

    }

    public void setMask(String maskUrl) {
        addAttribute(new SVGAttribute(AttributeType.mask, "url(#" + maskUrl + ")"));
    }

    public void addUseElem(String href, BigDecimal xCoord, BigDecimal yCoord) {
        UseTag tempTag = new UseTag(xCoord, yCoord);
        tempTag.addAttribute(new SVGAttribute(AttributeType.href,href));
        addChildElement(tempTag);
    }

    public static class Builder {

        private String maskUrl;

        private UseTag childUseTag;

        private String useHref;

        public Builder(String maskUrl) {
            this.maskUrl = maskUrl;
        }

        public Builder childUseTag(BigDecimal xCoord, BigDecimal yCoord) {
            childUseTag = new UseTag(xCoord, yCoord);
            return this;
        }

        public Builder useHref(String href) {
            this.useHref = href;
            return this;
        }

        public GroupTag build() {
            return new GroupTag(this);
        }
    }
}
