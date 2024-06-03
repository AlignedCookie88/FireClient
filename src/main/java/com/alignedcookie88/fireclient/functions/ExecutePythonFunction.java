package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.arguments.StringArgument;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import org.python.util.PythonInterpreter;

import java.util.ArrayList;
import java.util.List;

public class ExecutePythonFunction implements FireFunction {
    @Override
    public String getID() {
        return "execute_python";
    }

    @Override
    public String getName() {
        return "Execute Python";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        List<FireArgument> arguments = new ArrayList<>();
        arguments.add(new StringArgument("code"));
        return arguments;
    }

    @Override
    public void execute(Object[] providedArguments) {
        String code = (String) providedArguments[0];

        if (State.pythonExecution == null) {
            MinecraftClient.getInstance().setScreen(new ConfirmScreen((output) -> {
                State.pythonExecution = output;
                MinecraftClient.getInstance().setScreen(null);
                if (output) {
                    execute(providedArguments);
                }
            }, Text.literal("FireClient"), Text.literal("The current plot would like to run code on your computer. This can have legitimate use-cases, but can also be used for malicious purposes (e.g. malware). Only allow the plot to run this code if you trust it. Trust it as much as you would an exe file you downloaded from 8j39fj37fj48.com."), Text.literal("Allow"), Text.literal("Deny")));
        } else if (Boolean.TRUE.equals(State.pythonExecution)) {
            PythonInterpreter interpreter = new PythonInterpreter();
            interpreter.exec(code);
        }
    }
}
