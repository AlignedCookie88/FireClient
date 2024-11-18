package com.alignedcookie88.fireclient;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public interface FireFunction {
    public String getID();

    public String getName();

    public List<FireArgument> getExpectedArguments();

    public void execute(Object[] providedArguments);

    default String getDescription() {
        return "No description.";
    }

    default boolean hidden() {
        return false;
    }

    default String getWikiLink() {
        return null;
    }

    default String getSignName() {
        return getName().replace(" ", "");
    }
}
