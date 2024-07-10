package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;

import java.util.ArrayList;
import java.util.List;

public class EnableMovementFunction implements FireFunction {
    @Override
    public String getID() {
        return "enable_movement";
    }

    @Override
    public String getName() {
        return "Enable Movement";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return new ArrayList<>();
    }

    @Override
    public void execute(Object[] providedArguments) {
        State.canMove = true;
    }

    @Override
    public String getDescription() {
        return "Enable the player's character controls.";
    }
}
