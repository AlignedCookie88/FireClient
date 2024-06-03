package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.arguments.MiniMessageArgument;
import net.kyori.adventure.text.Component;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DebugFunction implements FireFunction {
    @Override
    public String getID() {
        return "debug";
    }

    @Override
    public String getName() {
        return "Debug Function";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        List<FireArgument> args = new ArrayList<>();
        args.add(new MiniMessageArgument("message"));
        return args;
    }

    @Override
    public void execute(Object[] providedArguments) {
        Component value = (Component) providedArguments[0];
        Utility.sendMessage(Text.literal("DEBUG: ").append(Utility.componentToText(value)));
    }
}
