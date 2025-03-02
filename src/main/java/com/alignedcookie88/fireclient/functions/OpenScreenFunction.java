package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.CustomScreen;
import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.arguments.IntegerArgument;
import com.alignedcookie88.fireclient.arguments.StringArgument;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class OpenScreenFunction implements FireFunction {
    @Override
    public String getID() {
        return "open_screen";
    }

    @Override
    public String getName() {
        return "Open Screen";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        List<FireArgument> arguments = new ArrayList<>();
        arguments.add(new IntegerArgument("width"));
        arguments.add(new IntegerArgument("height"));
        arguments.add(new StringArgument("name"));
        return arguments;
    }

    @Override
    public void execute(Object[] providedArguments) {
        Integer width = (Integer) providedArguments[0];
        Integer height = (Integer) providedArguments[1];
        String name = (String) providedArguments[2];
        CustomScreen screen = new CustomScreen(Text.literal(name), width, height, Identifier.of("fireclient", "textures/gui/default_screen_bg.png"));
        //MinecraftClient.getInstance().setScreen(screen);
    }

    @Override
    public String getDescription() {
        return "Opens a customisable screen";
    }
}
