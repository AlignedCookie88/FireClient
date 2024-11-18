package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.arguments.StringArgument;
import net.minecraft.util.Identifier;

import java.util.List;

public class SetPostProcessorFunction implements FireFunction {
    @Override
    public String getID() {
        return "set_post_processor";
    }

    @Override
    public String getName() {
        return "Set Post Processor";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new StringArgument("post_processor_id")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        String postProcessorId = (String) providedArguments[0];
        Identifier id = Identifier.tryParse(postProcessorId);
        State.setPostProcessor(id);
    }

    @Override
    public String getDescription() {
        return "Sets the post-processing effect.";
    }

    @Override
    public String getWikiLink() {
        return "https://github.com/AlignedCookie88/FireClient/wiki/Post%E2%80%90Processing-Shaders";
    }
}
