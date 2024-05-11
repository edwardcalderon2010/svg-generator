package com.ec.svg.generator.app.model.domain.path;

import com.ec.svg.generator.app.model.domain.enums.AxisPlane;
import com.ec.svg.generator.app.model.domain.enums.MathBound;
import com.ec.svg.generator.app.util.MathUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.ec.svg.generator.app.util.MathUtils.setScale;

public class CubicCurve {

    private static final Logger logger = LoggerFactory.getLogger(CubicCurve.class);

    @Getter
    @Setter
    private Point startPoint;

    @Getter
    @Setter
    private Point endPoint;

    @Getter
    @Setter
    private Point controlPointOne;

    @Getter
    @Setter
    private Point controlPointTwo;

    @Getter
    private BigDecimal width = new BigDecimal(0);

    @Getter
    private BigDecimal height = new BigDecimal(0);

    @Getter
    private BigDecimal minX;
    @Getter
    private BigDecimal maxX;
    @Getter
    private BigDecimal minY;
    @Getter
    private BigDecimal maxY;

    public CubicCurve(Point start, Point end, Point controlOne, Point controlTwo) {

        this.startPoint = start;
        this.endPoint = end;
        this.controlPointOne = controlOne;
        this.controlPointTwo = controlTwo;

        this.minX = getReferenceValue(ParameterContext.MIN_X);
        this.maxX = getReferenceValue(ParameterContext.MAX_X);
        this.minY = getReferenceValue(ParameterContext.MIN_Y);
        this.maxY = getReferenceValue(ParameterContext.MAX_Y);

        this.width = maxX.subtract(minX);
        this.height = maxY.subtract(minY);
    }

    public Point getBezierCurvePoint(Coordinate curveCoord) {

        // Calculate distance (percentage curve)
        // input param context will indicate the point & axis to scale from
        // Calculate difference between target point and start point

        BigDecimal difference = startPoint.getCoordinate(curveCoord.axisPlane()).value().subtract(curveCoord.value()).abs();
        BigDecimal distance = difference.divide((curveCoord.axisPlane().equals(AxisPlane.X) ? width : height), RoundingMode.DOWN);

        BigDecimal x = MathUtils.calculateBezierCurve(startPoint.getXValue(), endPoint.getXValue(), controlPointOne.getXValue(), controlPointTwo.getXValue(), distance);
        BigDecimal y = MathUtils.calculateBezierCurve(startPoint.getYValue(), endPoint.getYValue(), controlPointOne.getYValue(), controlPointTwo.getYValue(), distance);
        Point curvePoint = new Point(x,y);

        //logger.info("#### got BZ point: " + curvePoint.toString());
        return curvePoint;
    }

    public boolean containsCoord(Coordinate curveCoord) {
        // If a curve 'contains' a given parameter/point this does not imply the point
        // is also part of the bezier/cubic curve.
        // What is being calculated here is whether the height/width of a curve encompasses
        // the given point.
        Boolean contains = Boolean.FALSE;


        //logger.info("#### Testing contains curve at " + startPoint.toString() + ";" + endPoint.toString());
        BigDecimal startVal = startPoint.getCoordinate(curveCoord.axisPlane()).value();
        BigDecimal endVal = endPoint.getCoordinate(curveCoord.axisPlane()).value();

        BigDecimal maxVal = startVal.compareTo(endVal) > 0 ? startVal : endVal;
        BigDecimal minVal = startVal.compareTo(maxVal) == 0 ? endVal : startVal;

        if (curveCoord.value().compareTo(minVal) >= 0 &&
                curveCoord.value().compareTo(maxVal) <= 0) {
            contains = Boolean.TRUE;
            //logger.info("#### Got containment ");
        }
        //logger.info("#### Testing END ");

        return contains;
    }

    public BigDecimal getSize(AxisPlane axis) {
        return axis.equals(AxisPlane.X)? width : height;
    }
    public BigDecimal getReferenceValue(ParameterContext paramContext) {

        BigDecimal startValue = startPoint.getCoordinate(paramContext.getAxisPlane()).value();
        BigDecimal endValue = endPoint.getCoordinate(paramContext.getAxisPlane()).value();
        BigDecimal result = startValue;

        int compareVal = startValue.compareTo(endValue);

        if ( (paramContext.getMathBound().equals(MathBound.MAX) && compareVal < 0) ||
                (paramContext.getMathBound().equals(MathBound.MIN) && compareVal > 0) ) {
            result = endValue;
        }

        return result;
    }


    public CubicCurve compare(ParameterContext paramContext, CubicCurve target) {
        CubicCurve result = this;
        //logger.info(" >>>>>>> Comparing " + paramContext.getMathBound().name() + " " + paramContext.getAxisPlane().name() +
        //        " of \n" + this.toString() + " and \n" + target.toString());

        int compareRefVal = getReferenceValue(paramContext).compareTo(target.getReferenceValue(paramContext));

        Coordinate curveCoordinate = paramContext.getCurveCoordinate();
        if (curveCoordinate != null) {
            int compareBzPoint = getBezierCurvePoint(curveCoordinate).compareAxisValue(paramContext.getAxisPlane(), target.getBezierCurvePoint(curveCoordinate));

            compareRefVal = compareBzPoint;
        } else {
            if (compareRefVal == 0) {
                // If both curves have the same start or end point, compare curve size
                compareRefVal = getSize(paramContext.getAxisPlane()).compareTo(target.getSize(paramContext.getAxisPlane()));
                //logger.info("## Detected same start/end point, using size comparison: " + compareRefVal);
            }

        }

        if ( (paramContext.getMathBound().equals(MathBound.MAX) && compareRefVal < 0) ||
                (paramContext.getMathBound().equals(MathBound.MIN) && compareRefVal > 0) ) {
            result = target;
        }

        //logger.info(" <<<<<< Got comparison result " + result.toString());
        return result;
    }

    public String toString() {
        return "[Start:"+startPoint.toString()+
                "; End:"+endPoint.toString()+
                "; ControlOne:"+controlPointOne.toString()+
                "; ControlTwo:"+controlPointTwo.toString()+" ]";
    }

}
