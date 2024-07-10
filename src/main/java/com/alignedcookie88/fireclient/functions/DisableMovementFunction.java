package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;

import java.util.ArrayList;
import java.util.List;

public class DisableMovementFunction implements FireFunction {
    @Override
    public String getID() {
        return "disable_movement";
    }

    @Override
    public String getName() {
        return "Disable Movement";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return new ArrayList<>();
    }

    @Override
    public void execute(Object[] providedArguments) {
        State.canMove = false;
    }

    @Override
    public String getDescription() {
        return "Disables the player's character controls.";
    }
}
