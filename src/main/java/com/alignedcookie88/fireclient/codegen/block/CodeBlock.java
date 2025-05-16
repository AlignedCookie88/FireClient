package com.alignedcookie88.fireclient.codegen.block;

import com.alignedcookie88.fireclient.codegen.item.CodeItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CodeBlock {

    protected final List<CodeItem> items = new ArrayList<>();
    protected final List<CodeItem> endItems = new ArrayList<>();

    public abstract String getType();

    public abstract String getName();

    public String getAction() {
        return getName();
    }

    public JsonElement toJson() {
        JsonObject obj = new JsonObject();

        obj.add("id", new JsonPrimitive("block"));
        obj.add("block", new JsonPrimitive(getType()));

        JsonObject args = new JsonObject();
        JsonArray items = new JsonArray();
        int slot = 0;
        for (CodeItem item : this.items) {
            items.add(item.toJson(slot));
            slot++;
        }
        slot = 26;
        for (CodeItem item : this.endItems) {
            items.add(item.toJson(slot));
            slot--;
        }
        args.add("items", items);
        obj.add("args", args);

        addData(obj);

        return obj;
    }

    protected void addData(JsonObject obj) {

        obj.add("action", new JsonPrimitive(getName()));

    }

    public CodeBlock withItem(CodeItem item) {
        if (item.startsFromEnd())
            endItems.add(item);
        else items.add(item);
        return this;
    }

    public CodeBlock withSpecialisedItem(Function<CodeBlock, CodeItem> function) {
        return withItem(function.apply(this));
    }

}
