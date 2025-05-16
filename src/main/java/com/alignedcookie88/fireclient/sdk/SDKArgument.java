package com.alignedcookie88.fireclient.sdk;

import com.alignedcookie88.fireclient.codegen.Template;

public abstract class SDKArgument<T> {

    public static final String SPLITTER = "\uEFC0\uEFC1\uEFCF";
    public static final String SPLITTER_L2 = "\uEFC0\uEFC2\uEFCF";
    public static final String SPLITTER_L3 = "\uEFC0\uEFC3\uEFCF";

    private final String name;

    public SDKArgument(String name) {
        this.name = name;
    }

    protected T value;

    public abstract void parse(String input);

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public abstract String getHypercubeType();

    public void addBlocks(Template template) {

    }

}
