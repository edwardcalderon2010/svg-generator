package com.ec.svg.generator.app.model.domain.path;

import com.ec.svg.generator.app.model.domain.enums.CommandType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Command implements Cloneable {

    @Getter
    private final CommandType commandType;

    @Getter
    private List<Point> parameterList = new ArrayList<>();

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public void addParameter(Point param) {
        if (parameterList == null) {
            parameterList = new ArrayList<>();
        }
        parameterList.add(param);
    }

    public String toString() {
        String result = "";
        String paramString = null;

        result = commandType.name();

        if (parameterList != null && parameterList.size() > 0) {
            paramString = parameterList.stream().map(Point::toString).collect(Collectors.joining(" "));

            result += " " + paramString;
        }

        return result;
    }

    @Override
    public Command clone() throws CloneNotSupportedException {
        Command sourceCmd = this;
        Command clone = (Command) super.clone();
        List<Point> clonedPoints = new ArrayList<>();
        sourceCmd.getParameterList().forEach(pt -> {
            try {
                clonedPoints.add(pt.clone());
            } catch (CloneNotSupportedException cnse) {
                cnse.printStackTrace();
            }
        });
        clone.parameterList = clonedPoints;

        return clone;
    }
}
