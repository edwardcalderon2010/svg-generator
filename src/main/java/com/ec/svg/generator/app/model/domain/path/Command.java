package com.ec.svg.generator.app.model.domain.path;

import com.ec.svg.generator.app.model.domain.enums.CommandType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Command {

    @Getter
    private final CommandType commandType;

    @Getter
    private List<Point> parameterList;

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
}
