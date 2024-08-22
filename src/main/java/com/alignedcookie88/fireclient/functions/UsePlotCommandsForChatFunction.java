package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;

import java.util.List;

public class UsePlotCommandsForChatFunction implements FireFunction {
    @Override
    public String getID() {
        return "use_plot_commands_for_chat";
    }

    @Override
    public String getName() {
        return "Use Plot Commands For Chat";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of();
    }

    @Override
    public void execute(Object[] providedArguments) {
        State.plotCommandsForChat = true;
    }
}
