package com.alignedcookie88.fireclient.arguments;

import com.alignedcookie88.fireclient.FireArgument;

public class IntegerArgument implements FireArgument {
    String id;

    public IntegerArgument(String id) {
        this.id = id;
    }

    @Override
    public Object parse(String raw) {
        return Integer.valueOf(raw);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getDFType() {
        return "num";
    }
}
