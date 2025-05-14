package com.alignedcookie88.fireclient.legacy_sdk.functions;

import com.alignedcookie88.fireclient.legacy_sdk.FireArgument;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.IntegerArgument;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.MiniMessageArgument;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.StringArgument;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class ScreenAddButtonFunction implements FireFunction {
    @Override
    public String getID() {
        return "screen_add_button";
    }

    @Override
    public String getName() {
        return "Screen Add Button";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        List<FireArgument> arguments = new ArrayList<>();
        arguments.add(new MiniMessageArgument("button_text"));
        arguments.add(new IntegerArgument("x"));
        arguments.add(new IntegerArgument("y"));
        arguments.add(new IntegerArgument("width"));
        arguments.add(new IntegerArgument("height"));
        arguments.add(new StringArgument("click_command"));
        return arguments;
    }

    @Override
    public void execute(Object[] providedArguments) {
        Component button_text = (Component) providedArguments[0];
        Integer x = (Integer) providedArguments[1];
        Integer y = (Integer) providedArguments[2];
        Integer width = (Integer) providedArguments[3];
        Integer height = (Integer) providedArguments[4];
        String click_command = (String) providedArguments[5];
        if (State.screen != null) {
            State.screen.addButton(button_text, x, y, width, height, click_command);
        }
    }

    @Override
    public String getDescription() {
        return "Adds a button to a custom screen.";
    }
}
