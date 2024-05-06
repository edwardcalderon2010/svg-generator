package com.ec.svg.generator.app.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    public static final int DEFAULT_SCALE = 2;
    public static final RoundingMode DEFAULT_ROUND = RoundingMode.FLOOR;

    public static BigDecimal setScale(BigDecimal input) {
        return input.setScale(DEFAULT_SCALE,DEFAULT_ROUND);
    }

    public static BigDecimal calculateBezierCurve(BigDecimal start, BigDecimal end, BigDecimal controlCoordOne, BigDecimal controlCoordTwo, BigDecimal distance) {
        // Formula for calculation Bezier curve (from https://en.wikipedia.com/wiki/Bezier_curve):
        // B(t) = (1-t)exp3*P0 + 3*(1-t)exp2*t*P1 + 3*(1-t)*(t)exp2*P2 + (t)exp3*P3;
        // Where:
        // P0: start
        // P1: control pt #1
        // P2: control pt #2
        // P3: end

        BigDecimal result = null;
        BigDecimal inverseDist = new BigDecimal( "1.00").subtract(distance);
        BigDecimal p0Op = setScale(inverseDist.pow(3).multiply(start));

        BigDecimal p1Op = setScale(inverseDist.pow(2)
                .multiply(new BigDecimal("3.00"))
                .multiply(distance)
                .multiply(controlCoordOne));

        BigDecimal p2Op = setScale(inverseDist
                .multiply(new BigDecimal("3.00"))
                .multiply(distance.pow(2))
                .multiply(controlCoordTwo));

        BigDecimal p3Op = setScale(distance.pow(3).multiply(end));

        result = p0Op.add(p1Op).add(p2Op).add(p3Op);

        return result;
    }

}
