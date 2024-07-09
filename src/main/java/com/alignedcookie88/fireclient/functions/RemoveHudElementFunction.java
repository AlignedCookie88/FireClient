package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.arguments.StringArgument;
import com.alignedcookie88.fireclient.hud.HudElement;

import java.util.List;
import java.util.Objects;

public class RemoveHudElementFunction implements FireFunction {
    @Override
    public String getID() {
        return "remove_hud_element";
    }

    @Override
    public String getName() {
        return "RemoveHudElement";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new StringArgument("id")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        String id = (String) providedArguments[0];
        int to_remove = -1;
        int i = 0;
        for (HudElement element : State.hud) {
            if (Objects.equals(element.getID(), id)) {
                to_remove = i;
                break;
            }
            i++;
        }
        if (to_remove == -1)
            return;
        State.hud.remove(i);
    }
}
