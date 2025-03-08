package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.arguments.StringArgument;

import java.util.HashMap;
import java.util.List;

public class RebindCommandFunction implements FireFunction {

    public static final HashMap<String, String> commands = new HashMap<>();

    @Override
    public String getID() {
        return "rebind_command";
    }

    @Override
    public String getName() {
        return "Rebind Command";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new StringArgument("command"),
                new StringArgument("plot_command")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        String command = (String) providedArguments[0];
        String plot_command = (String) providedArguments[1];

        if (!commands.containsKey(command)) {
            Utility.sendStyledMessage(String.format("Cannot rebind command %s, allowed commands are: %s. Please note that rebinding a command rebinds all aliases of that command.", command, String.join(", ", commands.keySet())));
            return;
        }

        State.reboundCommands.put(commands.get(command), plot_command);
    }

    @Override
    public String getDescription() {
        return "Rebinds a command to a plot command.";
    }
}
