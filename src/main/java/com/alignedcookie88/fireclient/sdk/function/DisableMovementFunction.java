package com.alignedcookie88.fireclient.sdk.function;

import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.sdk.SDKArgument;
import com.alignedcookie88.fireclient.sdk.SDKFunction;
import net.minecraft.text.Text;

public class DisableMovementFunction extends SDKFunction {
    @Override
    public String getId() {
        return "disable_move";
    }

    @Override
    public Text getHumanName() {
        return Text.literal("Disable Movement");
    }

    @Override
    public void onExecute() {
        State.canMove = false;
    }

    @Override
    public SDKArgument<?>[] getArguments() {
        return new SDKArgument[0];
    }
}
