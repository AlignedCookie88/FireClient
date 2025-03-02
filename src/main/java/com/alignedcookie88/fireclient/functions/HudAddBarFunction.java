package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.arguments.FloatArgument;
import com.alignedcookie88.fireclient.arguments.IntegerArgument;
import com.alignedcookie88.fireclient.arguments.StringArgument;
import com.alignedcookie88.fireclient.hud.BarHudElement;

import java.util.List;

public class HudAddBarFunction implements FireFunction {
    @Override
    public String getID() {
        return "hud_add_bar";
    }

    @Override
    public String getName() {
        return "HUD Add Bar";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new StringArgument("id"),
                new FloatArgument("x"),
                new FloatArgument("y"),
                new IntegerArgument("x_offset"),
                new IntegerArgument("y_offset"),
                new IntegerArgument("width"),
                new FloatArgument("progress"),
                new IntegerArgument("colour")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        String id = (String) providedArguments[0];
        float x = (Float) providedArguments[1];
        float y = (Float) providedArguments[2];
        int x_offset = (Integer) providedArguments[3];
        int y_offset = (Integer) providedArguments[4];
        int width = (Integer) providedArguments[5];
        float progress = (Float) providedArguments[6];
        int colour = (Integer) providedArguments[7];
        State.hud.add(
                new BarHudElement(id, x, y, x_offset, y_offset, width, progress, colour)
        );
    }

    @Override
    public String getDescription() {
        return "Add a custom sized bossbar to the HUD.";
    }
}
