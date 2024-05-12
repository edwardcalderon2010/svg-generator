package com.ec.svg.generator.app.model.domain;

import com.ec.svg.generator.app.model.domain.enums.CommandType;
import com.ec.svg.generator.app.model.domain.path.*;

import static com.ec.svg.generator.app.model.domain.enums.CommandType.Z;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SVGPathSection implements Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(SVGPathSection.class);

    @Getter
    private List<Command> pathCommands = new ArrayList<>();

    @Getter
    private List<CubicCurve> cubicCurves = new ArrayList<>();

    public void addCommand(Command command) {
        if (pathCommands == null) {
            pathCommands = new ArrayList<>();
        }

        pathCommands.add(command);
        processCurves(command);
    }

    private void addCurve(CubicCurve cubicCurve) {
        cubicCurves.add(cubicCurve);
    }
    private void processCurves(Command command) {
        CubicCurve cubicCurve = null;
        if (command != null && command.getCommandType().equals(CommandType.C)) {
            // TODO: verify; if we are processing a C command at this point there MUST always be an M command present
            if (pathCommands != null && pathCommands.size() > 0 ) {
                Command mCommand = pathCommands.get(0);
                if (mCommand.getCommandType().equals(CommandType.M)) {
                    Point cubicStart = mCommand.getParameterList().get(0);
                    List<Point> curveParams = command.getParameterList();
                    curveParams
                            .stream()
                            .filter(param -> curveParams.indexOf(param) > 1 )
                            .filter(param -> (curveParams.indexOf(param)+1) % 3 == 0 )
                            .forEach(param -> addCurve(new CubicCurve(
                                    (curveParams.indexOf(param)-3) > 0 ? curveParams.get(curveParams.indexOf(param)-3) : cubicStart,
                                    param,
                                    curveParams.get(curveParams.indexOf(param)-2),
                                    curveParams.get(curveParams.indexOf(param)-1))));
                    //cubicCurves.forEach(elem -> logger.info("Got cubic curve: "+elem.toString()));
                }
            }
        }

    }

    public CubicCurve getReferenceCurve(ParameterContext paramContext) {
        List<CubicCurve> cubicCurves = getCubicCurves();
        CubicCurve result = null;

        result = cubicCurves
                .stream()
                .reduce((a,b) -> a.compare(paramContext,b))
                .orElse(null);

        if (result != null)  {
            logger.info("Got " + paramContext.getMathBound() + " curve: " + result.toString());
        }

        return result;

    }

    public CubicCurve getContainingCurve(ParameterContext paramContext) {
        List<CubicCurve> cubicCurves = getCubicCurves();
        CubicCurve result = null;

        result = cubicCurves
                .stream()
                .filter(curve -> curve.containsCoord(paramContext.getCurveCoordinate()))
                .reduce((a,b) -> a.compare(paramContext,b))
                .orElse(null);

        if (result != null)  {
            logger.info("Got container curve: " + result.toString());
        }

        return result;
    }

    public Boolean isClosedPath() {
        Boolean isClosed = Boolean.FALSE;

        if (pathCommands != null && pathCommands.size() > 0) {
            isClosed = pathCommands.stream().anyMatch(cmd -> cmd.getCommandType().equals(Z));
        }

        return isClosed;
    }

    public String toString() {
        String result = "";

        if (pathCommands != null && pathCommands.size() > 0) {
            result = pathCommands.stream().map(Command::toString).collect(Collectors.joining(" "));
        }

        return result;
    }

    @Override
    public SVGPathSection clone() throws CloneNotSupportedException {
        SVGPathSection sourceElem = this;
        SVGPathSection clone = (SVGPathSection) super.clone();

        List<Command> clonedCommands = new ArrayList<>();
        sourceElem.getPathCommands().forEach(cmd -> {
            try {
                clonedCommands.add(cmd.clone());
            } catch (CloneNotSupportedException cnse) {
                cnse.printStackTrace();
            }
        });
        clone.pathCommands = clonedCommands;

        List<CubicCurve> clonedCurves = new ArrayList<>();
        sourceElem.getCubicCurves().forEach(crv -> {
            try {
                clonedCurves.add(crv.clone());
            } catch (CloneNotSupportedException cnse) {
                cnse.printStackTrace();
            }
        });
        clone.cubicCurves = clonedCurves;

        return clone;
    }
}
