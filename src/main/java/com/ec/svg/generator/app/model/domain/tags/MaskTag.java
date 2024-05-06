package com.ec.svg.generator.app.model.domain.tags;

import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.enums.TagName;

public class MaskTag extends SVGElement {

    public MaskTag() {
        super(TagName.mask.name());
    }

}
