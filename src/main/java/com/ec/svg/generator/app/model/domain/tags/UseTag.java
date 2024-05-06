package com.ec.svg.generator.app.model.domain.tags;

import com.ec.svg.generator.app.interfaces.Moveable;
import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.SVGAttribute;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public class UseTag extends SVGElement implements Moveable {

    @Getter
    @Setter
    private BigDecimal xCoord;

    @Getter
    @Setter
    private BigDecimal yCoord;

    public UseTag() {
        this(new BigDecimal(0),new BigDecimal(0));
    }

    public UseTag(BigDecimal xCoord, BigDecimal yCoord) {
        super(TagName.use.name());
        this.xCoord = xCoord;
        this.yCoord = yCoord;

        addAttribute(new SVGAttribute(AttributeType.x, xCoord.toString()));
        addAttribute(new SVGAttribute(AttributeType.y, yCoord.toString()));
    }

    public void setHref(String value) {
        addAttribute(new SVGAttribute(AttributeType.href,value));
    }

    @Override
    public void applyXOffset(BigDecimal xOffset) {
        this.xCoord = xCoord.add(xOffset);
    }

    @Override
    public void applyYOffset(BigDecimal yOffset) {
        this.yCoord = yCoord.add(yOffset);
    }
}
