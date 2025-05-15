package com.alignedcookie88.fireclient.sdk.argument;

import com.alignedcookie88.fireclient.sdk.SDKArgument;

public class StringArgument extends SDKArgument<String> {

    public StringArgument(String name) {
        super(name);
    }

    @Override
    public void parse(String input) {
        value = input;
    }

    @Override
    public String getHypercubeType() {
        return "txt";
    }

}
