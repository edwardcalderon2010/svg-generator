package com.ec.svg.generator.app.interfaces;

import com.ec.svg.generator.app.model.domain.SVGAttribute;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import static com.ec.svg.generator.app.model.domain.enums.AttributeType.id;
import static com.ec.svg.generator.app.model.domain.enums.AttributeType.className;


public abstract class SVGElement implements XMLFragment {

    @Getter
    protected final String tagName;

    protected List<String> classNames;

    @Getter
    protected List<SVGElement> childElements;

    @Getter
    protected HashMap<AttributeType,SVGAttribute> attributeMap;

    public SVGElement(String tagName) {
        this.tagName = tagName;
    }

    public void addChildElement(SVGElement child) {
        if (childElements == null) {
            childElements = new ArrayList<>();
        }

        childElements.add(child);
    }

    public SVGElement getChildElement(TagName tagName) {
        SVGElement result = null;

        if (childElements != null) {
            result = childElements.stream().filter(elem -> elem.getTagName().equals(tagName.name())).findFirst().orElse(null);

        }

        return result;
    }

    public void addAttribute(SVGAttribute svgAttribute) {
        if (attributeMap == null) {
            attributeMap = new HashMap<>();
        }

        attributeMap.put(svgAttribute.getName(),svgAttribute);
    }

    public String getAttributeValue(AttributeType type) {
        String result = "";

        if (attributeMap != null) {
            result = attributeMap.get(type).getValue();
        }

        return result;
    }

    public SVGAttribute getAttribute(AttributeType type) {
        SVGAttribute result = null;

        if (attributeMap != null) {
            result = attributeMap.get(type);
        }

        return result;
    }

    public void addClassName(String name) {
        if (classNames == null) {
            classNames = new ArrayList<>();
        }
        classNames.add(name);
        addAttribute(new SVGAttribute(className,classNames.stream().collect(Collectors.joining(" "))));
    }

    public void setId(String name) {
        addAttribute(new SVGAttribute(id,name));
    }

    public String render() {
        String renderResult = "<" + getTagName() + " " +
                getAttributeMap().values().stream()
                        .map(SVGAttribute::render)
                        .collect(Collectors.joining(" "));

        if (childElements != null && childElements.size() > 0) {
            renderResult += " >\n" + childElements.stream()
                    .map(SVGElement::render)
                    .collect(Collectors.joining("\n"));
            renderResult += "\n</" + getTagName() + ">";
        } else {
            renderResult += "/>";
        }

        return renderResult;
    }

}
