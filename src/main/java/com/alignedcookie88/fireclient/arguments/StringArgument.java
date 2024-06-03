package com.alignedcookie88.fireclient.arguments;

import com.alignedcookie88.fireclient.FireArgument;

public class StringArgument implements FireArgument {

    String id;

    public StringArgument(String id) {
        this.id = id;
    }
    @Override
    public Object parse(String raw) {
        return (Object) raw;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getDFType() {
        return "txt";
    }
}
