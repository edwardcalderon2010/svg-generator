package com.ec.svg.generator.app.model.domain.path;

import com.ec.svg.generator.app.model.domain.enums.AxisPlane;
import lombok.Getter;

import java.math.BigDecimal;

public record Coordinate(@Getter BigDecimal value, @Getter AxisPlane axisPlane) {

    public String toString() {
        return value.toString();
    }
}
