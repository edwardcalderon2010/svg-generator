package com.ec.svg.generator.app.model.domain;

import com.ec.svg.generator.app.interfaces.XMLFragment;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import lombok.Getter;
import lombok.Setter;
import com.ec.svg.generator.app.util.StringUtils;
public class SVGAttribute implements XMLFragment {

    @Getter
    protected final AttributeType name;

    @Getter
    @Setter
    protected String value;

    public SVGAttribute(AttributeType name) {
        this.name = name;
    }

    public SVGAttribute(AttributeType name, String value) {
        this.name = name;
        this.value = value;
    }

    public String render() {
        String result = "";
        if (StringUtils.hasText(value)) {
            result = getName().getKey() + "=" + StringUtils.wrapQuotes(getValue());
        }

        return result;
    }
}
