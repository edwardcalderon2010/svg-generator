package com.ec.svg.generator.app.interfaces;

import com.ec.svg.generator.app.model.domain.SVGAttribute;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import com.ec.svg.generator.app.model.domain.tags.PathTag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import static com.ec.svg.generator.app.model.domain.enums.AttributeType.id;
import static com.ec.svg.generator.app.model.domain.enums.AttributeType.className;


public abstract class SVGElement implements XMLFragment, Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(SVGElement.class);

    @Getter
    protected final String tagName;

    @Getter
    protected List<String> classNames;

    @Getter
    protected List<SVGElement> childElements;

    @Getter
    protected Map<AttributeType,SVGAttribute> attributeMap;

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
            attributeMap = new LinkedHashMap<>();
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        SVGElement clonedElem = null;

        if (this instanceof PathTag) {

            PathTag sourceElem = (PathTag) this;
            logger.info("SVGElement: cloning from: " + sourceElem.getAttributeMap().get(id).getValue());

            clonedElem = (SVGElement) super.clone();

            if (sourceElem.getClassNames() != null && sourceElem.getClassNames().size() > 0) {
                clonedElem.classNames = new ArrayList<>(sourceElem.getClassNames());
            }

            if (sourceElem.getChildElements() != null && sourceElem.getChildElements().size() > 0) {
                List<SVGElement> clonedChildren = new ArrayList<>();
                sourceElem.getChildElements().stream().forEach(child -> {
                    try {
                        clonedChildren.add((SVGElement) child.clone());
                    } catch (CloneNotSupportedException cnse) {
                        cnse.printStackTrace();
                    }
                });
            }

            if (sourceElem.getAttributeMap() != null && sourceElem.getAttributeMap().size() > 0) {
                Map<AttributeType, SVGAttribute> clonedAttributes = new LinkedHashMap<>();
                sourceElem.getAttributeMap().forEach((k,v) -> {
                    try {
                        clonedAttributes.put(k,v.clone());
                    } catch (CloneNotSupportedException cnse) {
                        cnse.printStackTrace();
                    }
                });
                clonedElem.attributeMap = clonedAttributes;
            }

        }

        clonedElem.addClassName("justgotcloned");
        return clonedElem;
    }

}
