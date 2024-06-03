package com.alignedcookie88.fireclient;

import java.util.List;

public interface FireFunction {
    public String getID();

    public String getName();

    public List<FireArgument> getExpectedArguments();

    public void execute(Object[] providedArguments);
}
