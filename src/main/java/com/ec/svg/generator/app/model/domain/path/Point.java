package com.ec.svg.generator.app.model.domain.path;

import com.ec.svg.generator.app.interfaces.Moveable;
import com.ec.svg.generator.app.model.domain.enums.AxisPlane;
import com.ec.svg.generator.app.model.domain.enums.MathBound;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class Point implements Moveable, Cloneable {

    public static Point ZERO = new Point(BigDecimal.ZERO, BigDecimal.ZERO);
    @Getter
    @Setter
    private Coordinate xCoord;

    @Getter
    @Setter
    private Coordinate yCoord;

    private static final Logger logger = LoggerFactory.getLogger(Point.class);

    public Point() {
        this(new BigDecimal(0), new BigDecimal(0));
    }

    public Point(BigDecimal xCoord, BigDecimal yCoord) {
        this.xCoord = new Coordinate(xCoord, AxisPlane.X);
        this.yCoord = new Coordinate(yCoord, AxisPlane.Y);
    }

    public String toString() {
        String result = "";

        if (xCoord != null && yCoord != null) {
            result = xCoord + "," + yCoord;
        } else if (xCoord != null) {
            result = xCoord.toString();
        }

        return result;
    }

    @Override
    public void applyXOffset(BigDecimal xOffset) {
        // TODO: Handle a negative offset?
        this.xCoord = new Coordinate(xCoord.value().add(xOffset), AxisPlane.X);
        //logger.info("Updated xCoord to " + xCoord.toString());
    }

    @Override
    public void applyYOffset(BigDecimal yOffset) {
        // TODO: Handle a negative offset?
        this.yCoord = new Coordinate(yCoord.value().add(yOffset), AxisPlane.Y);
        //logger.info("Updated yCoord to " + yCoord.toString());
    }

    public Point compareReduce(ParameterContext paramContext, Point target) {
        Point result = this;

        BigDecimal sourceVal = this.getCoordinate(paramContext.getAxisPlane()).value();
        BigDecimal targetVal = target.getCoordinate(paramContext.getAxisPlane()).value();

        int compareVal = sourceVal.compareTo(targetVal);

        if ( (paramContext.getMathBound().equals(MathBound.MAX) && compareVal < 0) ||
                (paramContext.getMathBound().equals(MathBound.MIN) && compareVal > 0) ) {
            result = target;
        }


        return result;
    }

    public int compareAxisValue(AxisPlane axis, Point target) {

        BigDecimal sourceVal = this.getCoordinate(axis).value();
        BigDecimal targetVal = target.getCoordinate(axis).value();

        return sourceVal.compareTo(targetVal);
    }

    @Override
    public boolean equals(Object parameter) {
        boolean result = false;
        if (parameter instanceof Point) {
            Point input = (Point)parameter;
            if (xCoord.value().compareTo(input.getXCoord().value()) == 0
                && yCoord.value().compareTo(input.getYCoord().value()) == 0 ) {
                result = true;
            }
        }

        return result;
    }

    public Coordinate getCoordinate(AxisPlane axis) {
        return axis.equals(AxisPlane.X)? xCoord:yCoord;
    }

    public BigDecimal getXValue() {
        return getCoordinate(AxisPlane.X).value();
    }

    public BigDecimal getYValue() {
        return getCoordinate(AxisPlane.Y).value();
    }

    @Override
    public Point clone() throws CloneNotSupportedException {
        Point clone = (Point) super.clone();
        clone.setXCoord(new Coordinate(xCoord.getValue(),xCoord.axisPlane()));
        clone.setYCoord(new Coordinate(yCoord.getValue(),yCoord.axisPlane()));
        return clone;
    }
}
