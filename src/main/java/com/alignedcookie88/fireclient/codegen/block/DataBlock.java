package com.alignedcookie88.fireclient.codegen.block;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class DataBlock extends CodeBlock {

    private final String name;

    public DataBlock(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAction() {
        return "dynamic";
    }

    @Override
    protected void addData(JsonObject obj) {

        obj.add("data", new JsonPrimitive(getName()));

    }
}
