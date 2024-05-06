package com.ec.svg.generator.app.model.domain.enums;


import lombok.Getter;

public enum AttributeType {
    id("id"),
    className("class"),
    stroke("stroke"),
    strokeFill("fill"),
    strokeWidth("stroke-width"),
    d("d"),
    x("x"),
    y("y"),
    href("xlink:href"),
    mask("mask");

    @Getter
    private final String key;
    AttributeType(String key) {
        this.key = key;
    }
}
