package com.alignedcookie88.fireclient.functions;

import com.alignedcookie88.fireclient.FireArgument;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.State;

import java.util.List;

public class RemovePostProcessorFunction implements FireFunction {
    @Override
    public String getID() {
        return "remove_post_processor";
    }

    @Override
    public String getName() {
        return "Remove Post Processor";
    }

    @Override
    public List<FireArgument> getExpectedArguments() {
        return List.of();
    }

    @Override
    public void execute(Object[] providedArguments) {
        State.setPostProcessor(null);
    }

    @Override
    public String getDescription() {
        return "Removes the post-processing effect.";
    }

    @Override
    public String getWikiLink() {
        return "https://github.com/AlignedCookie88/FireClient/wiki/Post%E2%80%90Processing-Shaders";
    }

    @Override
    public String getSignName() {
        return "RemPostShader";
    }
}
