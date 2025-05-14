package com.alignedcookie88.fireclient.legacy_sdk.functions;

import com.alignedcookie88.fireclient.legacy_sdk.FireArgument;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.FloatArgument;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.IntegerArgument;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.MiniMessageArgument;
import com.alignedcookie88.fireclient.legacy_sdk.arguments.StringArgument;
import com.alignedcookie88.fireclient.hud.TextHudElement;
import net.kyori.adventure.text.Component;
import net.minecraft.text.Text;

import java.util.List;

public class HudAddTextFunction implements FireFunction {
    @Override
    public String getID() {
        return "hud_add_text";
    }

    @Override
    public String getName() {
        return "HUD Add Text";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new StringArgument("id"),
                new FloatArgument("x"),
                new FloatArgument("y"),
                new IntegerArgument("x_offset"),
                new IntegerArgument("y_offset"),
                new MiniMessageArgument("text")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        String id = (String) providedArguments[0];
        float x = (Float) providedArguments[1];
        float y = (Float) providedArguments[2];
        int x_offset = (Integer) providedArguments[3];
        int y_offset = (Integer) providedArguments[4];
        Component component = (Component) providedArguments[5];
        Text text = Utility.componentToText(component);
        State.hud.add(
                new TextHudElement(id, x, y, x_offset, y_offset, text)
        );
    }

    @Override
    public String getDescription() {
        return "Adds text to the player's HUD. x,y are 0-1";
    }
}
