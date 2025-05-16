package com.alignedcookie88.fireclient.codegen.item;

import com.alignedcookie88.fireclient.codegen.block.CodeBlock;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TagItem extends CodeItem {

    protected final String option;
    protected final String name;
    protected final String action;
    protected final String block;

    public TagItem(String name, String option, CodeBlock blockType) {
        this.option = option;
        this.name = name;
        this.action = blockType.getAction();
        this.block = blockType.getType();
    }

    @Override
    protected String getId() {
        return "bl_tag";
    }

    @Override
    protected JsonElement getData() {
        JsonObject obj = new JsonObject();

        obj.add("option", new JsonPrimitive(option));
        obj.add("tag", new JsonPrimitive(name));
        obj.add("action", new JsonPrimitive(action));
        obj.add("block", new JsonPrimitive(block));

        return obj;
    }

    @Override
    public boolean startsFromEnd() {
        return true;
    }
}
