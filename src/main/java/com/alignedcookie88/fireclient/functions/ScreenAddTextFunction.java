package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.arguments.IntegerArgument;
import com.alignedcookie88.fireclient.arguments.MiniMessageArgument;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ScreenAddTextFunction implements FireFunction {
    @Override
    public String getID() {
        return "screen_add_text";
    }

    @Override
    public String getName() {
        return "Screen Add Text";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new MiniMessageArgument("text"),
                new IntegerArgument("x"),
                new IntegerArgument("y")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        Component text = (Component) providedArguments[0];
        int x = (Integer) providedArguments[1];
        int y = (Integer) providedArguments[2];

        if (State.screen != null) {
            State.screen.addText(text, x, y);
        }
    }

    @Override
    public String getDescription() {
        return "Add text to a custom screen.";
    }
}
