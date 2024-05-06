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

    public void setMask(String maskUrl) {
        addAttribute(new SVGAttribute(AttributeType.mask, maskUrl));
    }

    public void addUseElem(String href, BigDecimal xCoord, BigDecimal yCoord) {
        UseTag tempTag = new UseTag(xCoord, yCoord);
        tempTag.addAttribute(new SVGAttribute(AttributeType.href,href));
        addChildElement(tempTag);
    }


}
