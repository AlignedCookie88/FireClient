package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.arguments.FloatArgument;
import com.alignedcookie88.fireclient.arguments.StringArgument;
import com.alignedcookie88.fireclient.hud.BarHudElement;
import com.alignedcookie88.fireclient.hud.HudElement;

import java.util.List;
import java.util.Objects;

public class HudSetBarProgressFunction implements FireFunction {
    @Override
    public String getID() {
        return "hud_set_bar_progress";
    }

    @Override
    public String getName() {
        return "HUD Set Bar Progress";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new StringArgument("id"),
                new FloatArgument("progress")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        String id = (String) providedArguments[0];
        float progress = (Float) providedArguments[1];

        for (HudElement element : State.hud) {
            if (Objects.equals(element.getID(), id)) {
                ((BarHudElement) element).setProgress(progress);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Set a custom bossbar's progress.";
    }
}
