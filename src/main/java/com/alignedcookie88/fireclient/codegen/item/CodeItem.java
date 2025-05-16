package com.alignedcookie88.fireclient.codegen.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class CodeItem {

    protected abstract String getId();

    protected abstract JsonElement getData();

    public JsonElement toJson(int slot) {
        JsonObject obj = new JsonObject();

        JsonObject item = new JsonObject();
        item.add("id", new JsonPrimitive(getId()));
        item.add("data", getData());
        obj.add("item", item);

        obj.add("slot", new JsonPrimitive(slot));

        return obj;
    }

    public boolean startsFromEnd() {
        return false;
    }

}
