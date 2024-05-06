package com.ec.svg.generator.app.model.domain.path;

import com.ec.svg.generator.app.model.domain.enums.AxisPlane;
import com.ec.svg.generator.app.model.domain.enums.MathBound;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

import static com.ec.svg.generator.app.model.domain.enums.AxisPlane.X;

public class ParameterContext {

    @Getter
    @Setter
    private MathBound mathBound;

    @Getter
    @Setter
    private AxisPlane axisPlane;

    @Getter
    @Setter
    private Comparator<Point> comparator;

    @Getter
    @Setter
    private Coordinate curveCoordinate;
    private static final Comparator<Point> paramXCompare = Comparator.comparing(Point::getXValue);
    private static final Comparator<Point> paramYCompare = Comparator.comparing(Point::getYValue);

    public static final ParameterContext MAX_X = new ParameterContext(MathBound.MAX, AxisPlane.X);
    public static final ParameterContext MAX_Y = new ParameterContext(MathBound.MAX, AxisPlane.Y);
    public static final ParameterContext MIN_X = new ParameterContext(MathBound.MIN, AxisPlane.X);
    public static final ParameterContext MIN_Y = new ParameterContext(MathBound.MIN, AxisPlane.Y);
    public ParameterContext(MathBound mathBound, AxisPlane coord) {

        this(mathBound,coord,null);
    }

    public ParameterContext(MathBound mathBound, AxisPlane axis, Coordinate curveCoordinate) {

        this.mathBound = mathBound;
        this.axisPlane = axis;
        this.curveCoordinate = curveCoordinate;

        init();
    }

    private void init() {

        comparator = paramXCompare;

        if (axisPlane.equals(AxisPlane.Y)) {
            comparator = paramYCompare;
        }
    }

    public MathBound getInverseMathBound() {
        return (this.mathBound.equals(MathBound.MAX)? MathBound.MIN : MathBound.MAX);
    }

    public AxisPlane getPerpendicularCoord() {
        return (this.axisPlane.equals(AxisPlane.X)? AxisPlane.Y : AxisPlane.X);
    }

}
