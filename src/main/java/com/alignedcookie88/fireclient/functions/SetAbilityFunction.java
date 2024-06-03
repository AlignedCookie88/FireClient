package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.arguments.IntegerArgument;
import com.alignedcookie88.fireclient.arguments.StringArgument;

import java.util.ArrayList;
import java.util.List;

public class SetAbilityFunction implements FireFunction {
    @Override
    public String getID() {
        return "set_ability";
    }

    @Override
    public String getName() {
        return "Set Ability";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        List<FireArgument> arguments = new ArrayList<>();
        arguments.add(new IntegerArgument("ability_id"));
        arguments.add(new StringArgument("function_to_call"));
        return arguments;
    }

    @Override
    public void execute(Object[] providedArguments) {
        Integer abilityId = (Integer) providedArguments[0];
        String functionToCall = (String) providedArguments[1];
        if (abilityId == 1)
            State.ability1Fn = functionToCall;
        else if (abilityId == 2)
            State.ability2Fn = functionToCall;
        else if (abilityId == 3)
            State.ability3Fn = functionToCall;
        else Utility.sendStyledMessage(String.format("The plot tried to register ability %d. Only abilities 1, 2 and 3 are valid.", abilityId));
    }
}
