package com.alignedcookie88.fireclient.legacy_sdk.functions;

import com.alignedcookie88.fireclient.*;
import com.alignedcookie88.fireclient.legacy_sdk.FireArgument;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.BooleanArgument;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.IntegerArgument;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunction;

import java.util.List;

public class ScreenAddSlot implements FireFunction {
    @Override
    public String getID() {
        return "screen_add_slot";
    }

    @Override
    public String getName() {
        return "Screen Add Slot";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new IntegerArgument("slot"),
                new IntegerArgument("x"),
                new IntegerArgument("y"),
                new BooleanArgument("interactable"),
                new BooleanArgument("background")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        Integer slot = (Integer) providedArguments[0];
        Integer x = (Integer) providedArguments[1];
        Integer y = (Integer) providedArguments[2];
        Boolean interactable = (Boolean) providedArguments[3];
        Boolean background = (Boolean) providedArguments[4];
        State.screen.addSlot(slot, x, y, interactable, background);
    }

    @Override
    public String getDescription() {
        return "Adds a slot to a screen.";
    }
}
