package com.alignedcookie88.fireclient.legacy_sdk.functions;

import com.alignedcookie88.fireclient.legacy_sdk.FireArgument;
import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunction;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.IntegerArgument;

import java.util.ArrayList;
import java.util.List;

public class ReportVersionFunction implements FireFunction {
    @Override
    public String getID() {
        return "report_version";
    }

    @Override
    public String getName() {
        return "Report Version";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        List<FireArgument> arguments = new ArrayList<>();
        arguments.add(new IntegerArgument("secret"));
        return arguments;
    }

    @Override
    public void execute(Object[] providedArguments) {
        Integer secret = (Integer) providedArguments[0];
        Utility.runPlotCommand(String.format("fireclient %d %s", secret, FireClient.version));
    }

    @Override
    public String getDescription() {
        return "Runs @fireclient <secret> <version>";
    }
}
