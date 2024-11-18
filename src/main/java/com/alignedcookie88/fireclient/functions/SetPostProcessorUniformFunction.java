package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.arguments.FloatArgument;
import com.alignedcookie88.fireclient.arguments.StringArgument;

import java.util.List;

public class SetPostProcessorUniformFunction implements FireFunction {
    @Override
    public String getID() {
        return "set_post_processor_uniform";
    }

    @Override
    public String getName() {
        return "Set Post Processor Uniform";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of(
                new StringArgument("name"),
                new FloatArgument("value")
        );
    }

    @Override
    public void execute(Object[] providedArguments) {
        String name = (String) providedArguments[0];
        float value = (Float) providedArguments[1];

        if (List.of("projmat", "screensize", "insize", "outsize", "time").contains(name.toLowerCase())) {
            Utility.sendStyledMessage("Cannot set post-processor uniform %s as it is prohibited by FireClient to prevent adverse affects on the client.".formatted(name));
            return;
        }

        if (name.toLowerCase().startsWith("fireclientinternal")) {
            Utility.sendStyledMessage("Cannot set post-processor uniforms that are meant to be internal to FireClient.");
            return;
        }

        if (State.postProcessor != null) {
            State.postProcessor.setUniforms(name, value);
        }
    }

    @Override
    public String getDescription() {
        return "Sets a post-processing uniform.";
    }

    @Override
    public String getWikiLink() {
        return "https://github.com/AlignedCookie88/FireClient/wiki/Post%E2%80%90Processing-Shaders";
    }
}
