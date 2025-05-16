package com.alignedcookie88.fireclient.codegen.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class StringItem extends CodeItem {

    private final String content;

    public StringItem(String content) {
        this.content = content;
    }

    @Override
    protected String getId() {
        return "txt";
    }

    @Override
    protected JsonElement getData() {
        JsonObject obj = new JsonObject();
        obj.add("name", new JsonPrimitive(content));
        return obj;
    }

}
