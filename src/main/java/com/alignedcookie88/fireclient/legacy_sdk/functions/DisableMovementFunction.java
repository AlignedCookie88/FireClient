package com.alignedcookie88.fireclient.legacy_sdk.functions;

import com.alignedcookie88.fireclient.legacy_sdk.FireArgument;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunction;
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
