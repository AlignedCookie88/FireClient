package com.alignedcookie88.fireclient.sdk.function;

import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.sdk.SDKArgument;
import com.alignedcookie88.fireclient.sdk.SDKFunction;
import net.minecraft.text.Text;

public class EnableMovementFunction extends SDKFunction {
    @Override
    public String getId() {
        return "enable_move";
    }

    @Override
    public Text getHumanName() {
        return Text.literal("Enable Movement");
    }

    @Override
    public void onExecute() {
        State.canMove = true;
    }

    @Override
    public SDKArgument<?>[] getArguments() {
        return new SDKArgument[0];
    }
}
